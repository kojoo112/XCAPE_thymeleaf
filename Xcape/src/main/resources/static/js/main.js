'use strict';

/**
 *  시작시 힌트리스트 받아오는 함수
 */
(function (){
    let object = {
        merchantCode: $("#merchant").val(),
        themeCode: $("#theme").val()
    }
    getHintList(object);
})();

/**
 * 힌트리스트 조회
 */
const source = $("#example").html();
const template = Handlebars.compile(source);

function getHintList(object){
    let hint = "";
    $.ajax({
        type: 'GET',
        url: '/api/getHintList',
        data: object,
        success: function (hintList) {
            $('#hintList').empty();
            let data = {
                hintList: hintList
            }
            let html = template(data);
            $('#hintList').append(html);
            $("#hintSize").val(hintList.length + 1);
        },
        error: console.log,
    })
}

/**
 * 테마리스트 조회
 */
const getThemeList = () => {
    let merchant = $('#merchant').val();
    $('#merchant').change(function () {
        $.ajax({
            type: 'GET',
            url: '/api/theme/list',
            data: 'merchantCode=' + merchant,
            success: function (data) {
                $('#theme').empty();
                $.each(data, function (key, value) {
                    let content = $(`<option value="${value.themeCode}">${value.themeName}</option>`);
                    $('#theme').append(content);
                })
            },
            error: console.log
        });
    })
}

/**
 * merchant, theme 변경시 힌트리스트 조회
 */
$("#merchant").change(function () {
    let object = {
        merchantCode: $("#merchant").val(),
        themeCode: 'THM001'
    }
    getHintList(object);
})

$("#theme").change(function () {
    let object = {
        merchantCode: $("#merchant").val(),
        themeCode: $("#theme").val()
    }
    getHintList(object);
})

/**
 * 힌트저장
 */
$('#hintRegisterButton').click(function(){
    let object = {
        message1: $('#message1').val(),
        message2: $('#message2').val(),
        themeCode: $('#theme').val(),
        key: "",
        merchant: $('#merchant').val(),
    }
    if(object.message1 != "" || object.message2 != "") {
        $.ajax({
            type: 'POST',
            url: '/api/registerHint',
            contentType: 'application/json',
            data: JSON.stringify(object),
            success: function () {
                let object = {
                    merchantCode: $("#merchant").val(),
                    themeCode: $("#theme").val()
                }
                alert('🌈 힌트가 성공적으로 등록되었습니다.');
                getHintList(object);
            },
            error: function (err) {
                alert('😭 등록에 실패했습니다.');
                console.log(err);
            }
        })
    } else {
        alert('❗️ 저장할 힌트를 입력해주세요 ❗️');
    }
})

/**
 *  힌트 삭제
 */
const deleteHint = (id) => {
        let merchantTheme = {
            merchantCode: $("#merchant").val(),
            themeCode: $("#theme").val()
        }
        if(confirm('힌트를 삭제하시겠습니까?')){
            $.ajax({
                type: 'POST',
                url: '/api/deleteHint',
                contentType: 'application/json',
                data: JSON.stringify({seq: id}),
                success: function () {
                    alert('🌈 힌트가 삭제되었습니다.');
                    getHintList(merchantTheme);
                },
                error: function (err){
                    alert('😭 삭제 실패했습니다.');
                    console.log(err);
                }
            })
        }
}

const modifyHint = (seq, name, message) => {
    let modifiedMessage = prompt('💻 수정할 내용을 입력해주세요.', message);
    if(modifiedMessage != null) {
        if (modifiedMessage != message) {
            let object = {
                [name]: modifiedMessage,
                seq: seq
            }
            let merchantTheme = {
                merchantCode: $("#merchant").val(),
                themeCode: $("#theme").val()
            }
            $.ajax({
                type: 'POST',
                url: '/api/modifyMessage',
                contentType: 'application/json',
                data: JSON.stringify(object),
                success: function () {
                    alert('🔥 힌트가 변경되었습니다.');
                    getHintList(merchantTheme);
                },
                error: function (err){
                    alert('😭 변경에 실패했습니다.');
                    console.log(err);
                }
            })
        }
    }
}

const modifyHintCode = (seq, key) => {
    let modifiedHintCode = prompt('💻 수정할 내용을 입력해주세요.', key);
    if(modifiedHintCode != null){
        if(modifiedHintCode != key){
            let object = {
                seq: seq,
                key: modifiedHintCode
            }
            let merchantTheme = {
                merchantCode: $("#merchant").val(),
                themeCode: $("#theme").val()
            }
            $.ajax({
                type:'POST',
                url:'/api/modifyHintCode',
                // contentType: 'application/json',
                data: object,
                statusCode: {
                    200: function () {
                        alert('🔥 힌트가 변경되었습니다.');
                        getHintList(merchantTheme);
                    },
                    202: function () {
                        alert('🙅 중복된 힌트코드입니다.');
                    }
                },
                error: function (err) {
                    alert('😭 변경에 실패했습니다.');
                    console.log(err);
                }
            })
        }
    }
}