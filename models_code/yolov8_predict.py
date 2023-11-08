import json
from ultralytics import YOLO


model = YOLO('../models/best.pt')


def tojson(self, normalize=False):
    """Convert the object to JSON format."""
    if self.probs is not None:
        LOGGER.warning('Warning: Classify task do not support `tojson` yet.')
        return

    import json

    # Create list of detection dictionaries
    results = []
    data = self.boxes.data.cpu().tolist()
    h, w = self.orig_shape if normalize else (1, 1)
    for i, row in enumerate(data):  # xyxy, track_id if tracking, conf, class_id
        box = {'x1': row[0] / w, 'y1': row[1] / h, 'x2': row[2] / w, 'y2': row[3] / h}
        conf = row[-2]
        class_id = int(row[-1])
        name = self.names[class_id]
        result = {'name': name, 'class': class_id, 'confidence': conf, 'box': box}
        if self.boxes.is_track:
            result['track_id'] = int(row[-3])  # track ID
        if self.masks:
            x, y = self.masks.xy[i][:, 0], self.masks.xy[i][:, 1]  # numpy array
            result['segments'] = {'x': (x / w).tolist(), 'y': (y / h).tolist()}
        if self.keypoints is not None:
            x, y, visible = self.keypoints[i].data[0].cpu().unbind(dim=1)  # torch Tensor
            result['keypoints'] = {'x': (x / w).tolist(), 'y': (y / h).tolist(), 'visible': visible.tolist()}
        results.append(result)

    # Convert detections to JSON
    return json.dumps(results, indent=2)


def predict(image_path):
    results = model(image_path)

    to_json = tojson(results[0])
    json_object = json.loads(to_json)

    class_list = []

    for i in range(len(json_object)):
        name = json_object[i]['name']
        if name in class_list:
            pass
        else:
            class_list.append(name)

    return class_list