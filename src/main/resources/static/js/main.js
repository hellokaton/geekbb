(function(){
    NProgress.configure({ ease: 'ease', speed: 500 });
    document.onreadystatechange = function(){
        NProgress.start();
        if(document.readyState == "Uninitialized"){
            NProgress.set(1);
        }
        if(document.readyState == "Interactive"){
            NProgress.set(0.5);
        }
        if(document.readyState == "complete"){
            NProgress.done();
        }
    }
})();

function alertInfo(msg){
    swal("提示信息", msg, "info" )
}

function alertOk(msg){
    swal("提示信息", msg, "success" )
}

function alertWarn(msg){
    swal("提示信息", msg, "warn" )
}

function alertError(msg){
    swal("提示信息", msg, "error" )
}

$(document).ready(function() {

    $('.new-topic').click(function () {
        if(LOGIN_USER === ""){
            $('#signinModalPopovers').modal({
                keyboard: true
            });
            return
        }
        window.location.href = '/topic/new';
    });

    /* TOPIC DETAIL */
    $('.social-share-btn').on('click', function () {
        $('.social-share').slideToggle();
    });

    if($('.social-share-btn').length > 0){
        socialShare('.social-share', {
            url: 'https://geek-dev.club' + window.location.pathname,
            sites: ['weibo', 'wechat', 'twitter', 'qq', 'facebook', 'google']
        });
    }

    var textarea = document.getElementById('content');
    var mditor;
    if (textarea) {
        mditor = Mditor.fromTextarea(textarea);
        mditor.height = '250px';
        mditor.split = false;

        mditor.on('ready', function () {
            mditor.toolbar.removeItem('help');
            mditor.toolbar.removeItem('togglePreview');
        });
    }

    $('#reply-topic').click(function () {
        var content = $('#comment-form #content').val();
        if (!content || content == '') {
            alertInfo('请输入回复内容');
            return;
        }
        var tid = $('#comment-form #tid').val();
        var owner = $('#comment-form #owner').val();
        var _this = $(this);
        _this.attr('disabled', true);

        $.post('/topic/comment', {
            tid: tid,
            content: content,
            owner: owner
        }, function (result) {
            _this.removeAttr('disabled');
            if (result.success) {
                $('#comment-form #content').val('');
                window.location.reload();
            } else {
                alertError(result.msg);
            }
        });
    });

    $('.topic-love').click(function () {
        var tid = $('.topic-detail').attr('tid');
        var _this = $(this);
        _this.attr('disabled', true);
        $.post('/topic/love/' + tid, function (result) {
            _this.removeAttr('disabled');
            if (result.success) {
                // 点赞成功
                if (result.payload === 1) {
                    _this.find('i').removeClass('czs-thumbs-up-l').addClass('czs-thumbs-up');
                    var text = $('span.loves').text();
                    $('span.loves').text((parseInt(text) + 1));
                } else {
                    // 取消点赞成功
                    _this.find('i').removeClass('czs-thumbs-up').addClass('czs-thumbs-up-l');
                    var text = $('span.loves').text();
                    $('span.loves').text((parseInt(text) - 1));
                }
            } else {
                if (result.code === 2001) {
                    alertError('请登录后尝试');
                }
            }
        });
    });

    $('.topic-collect').click(function () {
        var tid = $('.topic-detail').attr('tid');
        var _this = $(this);
        _this.attr('disabled', true);
        $.post('/topic/collect/' + tid, function (result) {
            _this.removeAttr('disabled');
            if (result.success) {
                // 收藏成功
                if (result.payload === 1) {
                    _this.find('i').removeClass('czs-bookmark-l').addClass('czs-bookmark');
                    var text = $('span.collects').text();
                    $('span.collects').text((parseInt(text) + 1));
                } else {
                    // 取消收藏成功
                    _this.find('i').removeClass('czs-bookmark').addClass('czs-bookmark-l');
                    var text = $('span.collects').text();
                    $('span.collects').text((parseInt(text) - 1));
                }
            } else {
                if (result.code === 2001) {
                    alertError('请登录后尝试');
                }
            }
        });
    });

    $('#publish-btn').click(function () {
        var title = $('#title').val();
        var nid = $('#nid').val();
        var content = $('#content').val();

        if (title === '') {
            alertInfo('请输入标题');
            $('#title').focus();
            return;
        }
        if (title.length < 5) {
            alertInfo('标题至少5个字符');
            $('#title').focus();
            return;
        }
        if (title.length > 50) {
            alertInfo('标题最多50个字符');
            $('#title').focus();
            return;
        }
        if (nid === '') {
            alertInfo('请选择发布的节点');
            return;
        }
        if (content === '') {
            alertInfo('请输入主题内容');
            $('#content').focus();
            return;
        }
        if (content.length < 5) {
            alertInfo('至少5个字符');
            $('#content').focus();
            return;
        }

        var _this = $(this);
        _this.attr('disabled', true);

        $.post('/topic/save', {
            title: title,
            content: content,
            topicType: $('#topicType').val(),
            nodeId: nid,
            nodeTitle: $("#nid").find("option:selected").text()
        }, function (result) {
            _this.removeAttr('disabled');
            if (result.success) {
                window.location.href = '/t/' + result.payload;
            } else {
                alertError(result.msg);
            }
        });
    });

    $('.save-setting-btn').click(function () {
        var _this = $(this);
        _this.attr('disabled', true);
        var params = $('#settings-form').serialize();
        $.post('/settings', params, function (result) {
            _this.removeAttr('disabled');
            if(result.success){
                alertOk('保存成功');
            } else {
                alertError(result.msg || '保存失败');
            }
        });
    });

});

function switchTopicType(topicType) {
    $('#topicType').val(topicType);
    if(topicType === 'TOPIC'){
        $('#topicType2').removeClass('badge-dark').addClass('badge-light');
        $('#topicType1').removeClass('badge-light badge-dark').addClass('badge-dark');
    } else {
        $('#topicType1').removeClass('badge-dark').addClass('badge-light');
        $('#topicType2').removeClass('badge-light badge-dark').addClass('badge-dark');
    }
}
