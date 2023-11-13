from roboflow import Roboflow
rf = Roboflow(api_key="28c9QEP4UQjYhws25hLV")
project = rf.workspace().project("foods-950o2")
model = project.version(3).model


def predict(image_path):
    prob = model.predict(image_path, confidence=40, overlap=30).json()

    class_list = []

    for i in range(len(prob ['predictions'])):
        name = prob['predictions'][i]['class']
        if name in class_list:
            pass
        else:
            class_list.append(name)

    return class_list




# infer on a local image
# print(model.predict("your_image.jpg", confidence=40, overlap=30).json())

# visualize your prediction
# model.predict("your_image.jpg", confidence=40, overlap=30).save("prediction.jpg")

# infer on an image hosted elsewhere
# print(model.predict("URL_OF_YOUR_IMAGE", hosted=True, confidence=40, overlap=30).json())