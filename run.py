import json
from ultralytics import YOLO
from ultralytics.utils import LOGGER
from googletrans import Translator
from flask import Flask, request, jsonify
from PIL import Image
import io

app = Flask(__name__)

translator = Translator()
model = YOLO('best.pt')

def to_json(results, normalize=False):
    """Convert the object to JSON format."""
    if results.probs is not None:
        LOGGER.warning('Warning: Classify task do not support `to_json` yet.')
        return

    # Create list of detection dictionaries
    results_list = []
    data = results.boxes.data.cpu().tolist()
    h, w = results.orig_shape if normalize else (1, 1)
    for row in data:
        box = {'x1': row[0] / w, 'y1': row[1] / h, 'x2': row[2] / w, 'y2': row[3] / h}
        conf = row[-2]
        class_id = int(row[-1])
        name = results.names[class_id]
        result = {'name': name, 'class': class_id, 'confidence': conf, 'box': box}
        if results.boxes.is_track:
            result['track_id'] = int(row[-3])  # track ID
        if results.masks:
            x, y = results.masks.xy[i][:, 0], results.masks.xy[i][:, 1]  # numpy array
            result['segments'] = {'x': (x / w).tolist(), 'y': (y / h).tolist()}
        if results.keypoints is not None:
            x, y, visible = results.keypoints[i].data[0].cpu().unbind(dim=1)
            result['keypoints'] = {'x': (x / w).tolist(), 'y': (y / h).tolist(), 'visible': visible.tolist()}
        results_list.append(result)

    # Convert detections to JSON
    return json.dumps(results_list, indent=2)

def predict(image_path):
    results = model(image_path)
    to_json_result = to_json(results[0])
    json_object = json.loads(to_json_result)
    class_list = list(set(item['name'] for item in json_object))
    return class_list

@app.route('/processImage', methods=['POST'])
def process_image():
    if 'image' not in request.files:
        return 'No image found', 400

    image = request.files['image']
    image_pil = Image.open(io.BytesIO(image.read()))
    desired_size = (640, 640)
    image_pil = image_pil.resize(desired_size, Image.BILINEAR)

    output = predict(image_pil)
    return jsonify(output)

@app.route('/translate', methods=['POST'])
def translate_text():
    try:
        data = request.get_json()
        text = data['text']
        source_lang = data.get('source_lang', 'auto')
        target_lang = data.get('target_lang', 'ko')
        translated = translator.translate(text, src=source_lang, dest=target_lang)
        return translated.text
    except Exception as e:
        return jsonify({'error': str(e)})

if __name__ == '__main__':
    app.run()
