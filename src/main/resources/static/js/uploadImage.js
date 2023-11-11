function uploadImage() {
    var input = document.getElementById('imageInput');
    var preview = document.getElementById('preview');

    var file = input.files[0];
    var reader = new FileReader();

    reader.onload = function (e) {
        var imageData = e.target.result;

        // 이미지 데이터를 localStorage에 저장
        localStorage.setItem('imageData', imageData);

        preview.src = imageData;
    };

    reader.readAsDataURL(file);
}
