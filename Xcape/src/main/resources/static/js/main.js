'use strict';

/**
 *  시작시 힌트리스트 받아오는 함수
 */
$(function (){
    let object = {
        merchantCode: $("#merchant").val(),
        themeCode: $("#theme").val()
    }
    getHintList(object);
    console.log('getHint');
});

/**
 * 힌트리스트 조회
 */
const source = $("#example").html();
const template = Handlebars.compile(source);

function getHintList(object){
    let hint = "";
    $.ajax({
        type: 'GET',
        url: '/getHint',
        data: object,
        success: function (hintList) {
            $('#hintList').empty();
            $.each(hintList, function (idx, val) {
                hint = template(val);
                $('#hintList').append(hint);
            })
            $("#hintSize").val(hintList.length + 1);
            console.log('생성직후');
        },
        error: console.log,
        complete: function(){
            deleteHint();
            console.log('deletebutton');
        }
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
            url: '/theme/list',
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
        // themeName: $('#theme option:selected').text(),
        key: "",
        merchant: $('#merchant').val(),
        // merchantName: $('#merchant option:selected').text()
    }
    $.ajax({
        type: 'POST',
        url: '/registerHint',
        contentType: 'application/json',
        data: JSON.stringify(object),
        success: function (hintList) {
            console.log(hintList);
            let object = {
                merchantCode: $("#merchant").val(),
                themeCode: $("#theme").val()
            }
            alert('힌트가 성공적으로 등록되었습니다.');
            getHintList(object);
        },
        error: console.log
    })
})

/**
 *  힌트 삭제
 */
const deleteHint = () => {
    $('.deleteButton').click(function(){
        let seq = $(this).attr('id');
        let object = {
            seq: seq
        }
        let merchantTheme = {
            merchantCode: $("#merchant").val(),
            themeCode: $("#theme").val()
        }
        if(confirm('힌트를 삭제하시겠습니까?')){
            // console.log($(this).attr('id'));
            $.ajax({
                type: 'POST',
                url: '/deleteHint',
                contentType: 'application/json',
                data: JSON.stringify(object),
                success: function () {
                    alert('힌트가 삭제되었습니다.');
                    getHintList(merchantTheme);
                    console.log(merchantTheme);
                }
            })
        }
    })
}