'use strict';
const API_HINT_URL = '/api/hint';
const API_THEME_URL = '/api/theme';

/**
 *  시작시 힌트리스트 받아오는 함수
 */
window.onload = function () {
    let getHintObject = {
        merchant: $("#merchant").val(),
        themeCode: $("#theme").val(),
        companyName: companyName
    }
    getHintList(getHintObject);
};

/**
 * 힌트리스트 조회
 */
const source = $("#example").html();
const template = Handlebars.compile(source);

function getHintList(object) {
    let comAjax = new ComAjax(METHOD.GET, `${API_HINT_URL}/list`, object);

    comAjax.setSuccessCallback((hintList) => {
        $('#hintList').empty();
        let data = {
            hintList: hintList.info
        }
        let html = template(data);
        $('#hintList').append(html);
        $("#hintSize").val(hintList.info.length + 1);
    });
    comAjax.setErrorCallback((err) => {
        console.log(err);
    });
    comAjax.getResult();
}

/**
 * 테마리스트 조회
 */
const getThemeList = () => {
    let parameter = {}
    parameter.merchantCode = $('#merchant').val();

    let merchantCode = $('#merchant').val();

    let comAjax = new ComAjax(METHOD.GET, `${API_THEME_URL}/list`, parameter);

    comAjax.setSuccessCallback((data) => {
        $('#theme').empty();
        $.each(data.info, function (key, value) {
            let content = $(`<option value="${value.themeCode}">${value.themeName}</option>`);
            $('#theme').append(content);
        });
        let getHintObject = {
            merchant: merchantCode,
            themeCode: $("#theme").val(),
            companyName: companyName
        }
        getHintList(getHintObject);
    });

    comAjax.setErrorCallback((err) => {
        console.log(err);
    });

    comAjax.getResult();

}

/**
 * theme 변경시 힌트리스트 조회
 */

$("#theme").change(function () {
    let object = {
        merchant: $("#merchant").val(),
        themeCode: $("#theme").val(),
        companyName: companyName
    }
    getHintList(object);
})

/**
 * 힌트저장
 */
$('#hintRegisterButton').click(function () {
    let object = {
        message1: $('#message1').val(),
        message2: $('#message2').val(),
        themeCode: $('#theme').val(),
        key: "",
        merchant: $('#merchant').val(),
        companyName: companyName
    }
    const pattern_specialChar = /[`]/;
    if (!pattern_specialChar.test(object.message1) && !pattern_specialChar.test(object.message2)) {
        if (object.message1 != "" || object.message2 != "") {
            let comAjax = new ComAjax(METHOD.POST, API_HINT_URL, object);

            comAjax.setSuccessCallback(() => {
                let getHintObject = {
                    merchant: $("#merchant").val(),
                    themeCode: $("#theme").val(),
                    companyName: companyName
                }
                alert('🌈 힌트가 성공적으로 등록되었습니다.');
                getHintList(getHintObject);
            });
            comAjax.setErrorCallback((err) => {
                console.log(err);
            });
            comAjax.getResult();
        } else {
            alert('❗️ 저장할 힌트를 입력해주세요 ❗️');
        }
    } else {
        alert('❗️ \`은 사용할 수 없습니다 ❗️')
    }
})

/**
 *  힌트 삭제
 */
const deleteHint = (id) => {

    let getHintObject = {
        merchant: $("#merchant").val(),
        themeCode: $("#theme").val(),
        companyName: companyName
    }

    if (confirm('힌트를 삭제하시겠습니까?')) {
        let comAjax = new ComAjax(METHOD.DELETE, `${API_HINT_URL}/${id}`, getHintObject);
        comAjax.setSuccessCallback(() => {
            alert('🌈 힌트가 삭제되었습니다.');
            getHintList(getHintObject);
        });
        comAjax.setErrorCallback((err) => {
            alert('😭 삭제 실패했습니다.');
            console.log(err);
        });
    }
}

const modifyHintMessage = (seq, name, message) => {
    let modifiedMessage = prompt('💻 수정할 내용을 입력해주세요.', message);

    const pattern_specialChar = /[`]/;
    if (!pattern_specialChar.test(modifiedMessage)) {
        if (modifiedMessage != null) {
            if (modifiedMessage != message) {
                let modifyMessageObject = {
                    [name]: modifiedMessage,
                    seq: seq,
                }
                let getHintObject = {
                    merchant: $("#merchant").val(),
                    themeCode: $("#theme").val(),
                }
                let comAjax = new ComAjax(METHOD.PATCH, '/api/message', modifyMessageObject);
                comAjax.setSuccessCallback(() => {
                    alert('🔥 힌트가 변경되었습니다.');
                    getHintList(getHintObject);
                });
                comAjax.setErrorCallback((err) => {
                    alert('😭 변경에 실패했습니다.');
                    console.log(err);
                });
                comAjax.getResult();
            }
        }
    }
}

const modifyHintCode = (seq, key) => {
    let modifiedHintCode = prompt('💻 수정할 내용을 입력해주세요.', key);
    if (modifiedHintCode != null) {
        if (modifiedHintCode != key) {
            let parameter = {
                seq: seq,
                key: modifiedHintCode,
            }
            let getHintObject = {
                merchant: $("#merchant").val(),
                themeCode: $("#theme").val(),
            }
            let comAjax = new ComAjax(METHOD.PATCH, `${API_HINT_URL}-code`, parameter);
            comAjax.setSuccessCallback()
            $.ajax({
                type: 'PATCH',
                url: '/api/hint-code',
                // contentType: 'application/json',
                data: parameter,
                statusCode: {
                    200: function () {
                        alert('🔥 힌트코드가 변경되었습니다.');
                        getHintList(getHintObject);
                    },
                    400: function () {
                        alert('🙅 중복된 힌트코드입니다.');
                    },
                    500: function () {
                        alert('😭 변경에 실패했습니다.');
                        console.log(err);
                    }
                }
            });
        }
    }
}
