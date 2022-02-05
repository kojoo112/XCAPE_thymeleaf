'use strict';
const METHOD = {GET: 'GET', POST: 'POST', PUT: 'PUT', PATCH: 'PATCH', DELETE: 'DELETE'};
const CONTENT_TYPE = 'application/json; charset=utf-8';

class ComAjax {
    url;
    type;
    data;
    contentType;
    success;
    error;
    complete;

    constructor(type, url, data) {
        this.type = type;
        this.url = url;
        this.data = data;
    }

    setSuccessCallback(success) {
        this.success = success;
    }

    getSuccessCallback() {
        if (this.success == undefined || this.success == '') {
            return '';
        }
        return this.success;
    }

    setErrorCallback(error) {
        this.error = error;
    }

    getErrorCallback() {
        if (this.error == undefined || this.error == '') {
            return '';
        }
        return this.error;
    }

    getData() {
        if (this.type != METHOD.GET) {
            return JSON.stringify(this.data);
        }
        return this.data;
    }

    setCompleteCallback(complete) {
        this.complete = complete;
    }

    getCompleteCallback() {
        if (this.complete == undefined || this.complete == '') {
            return '';
        }
        return this.complete;
    }

    setContentType() {
        if (this.type != METHOD.GET) {
            return CONTENT_TYPE;
        } else {
            return '';
        }
    }

    getResult() {
        return $.ajax({
            type: this.type,
            url: this.url,
            data: this.getData(),
            contentType: this.setContentType(),
            success: this.getSuccessCallback(),
            error: this.getErrorCallback(),
            complete: this.getCompleteCallback()
        });
    }
}
