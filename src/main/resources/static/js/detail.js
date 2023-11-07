
// 파일 확장자를 체크하고 이미지 파일만 허용
function setThumbnail(input) {
    var pathpoint = input.value.lastIndexOf('.');
    var filepoint = input.value.substring(pathpoint + 1, input.value.length);
    var filetype = filepoint.toLowerCase();

    if (filetype === 'jpg' || filetype === 'png' || filetype === 'jpeg') {
        // 이미지 파일일 경우 처리
        // 예를 들어 이미지 미리보기 업데이트 등을 수행
        var reader = new FileReader();
        reader.onload = function (e) {
            $('#view').attr('src', e.target.result);
        };
        reader.readAsDataURL(input.files[0]);
    } else {
        // 이미지 파일이 아닌 경우
        alert('첨부파일은 jpg, png, jpeg로 된 이미지만 허용됩니다.');
        // 파일 선택 input 초기화
        input.value = '';
    }
}
