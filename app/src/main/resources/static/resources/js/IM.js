/**
 * Created by C0dEr on 2017/5/26.
 */
var reqestFriend = $("#requestFriend").clone();
$("#requestFriend").remove();
var info = $("#info").clone();
$("#info").remove();
var searchpanel = $("#searchFriend").clone();
$("#searchFriend").remove();
var usepanel = $("#userpanel").clone();
$("#userpanel").remove();
var conve = $("#chat").clone();
$("#chat").remove();
var addpanel = $('#addpanel').clone();
$('#addpanel').remove();
var judge = $("#judgepanel").clone();
$("#judgepanel").remove();
var trend = $("#trendpanel").clone();
$("#trendpanel").remove();
$(function () {
    $.widget("moogle.contextmenu2", $.moogle.contextmenu, {});
    //region 个人信息弹窗

    $("#infomation").on("click", function () {
        layer.open({
            type: 1 //Page层类型
            , area: ['400px', '600px']
            , title: '个人信息'
            , shade: 0.0 //遮罩透明度
            , maxmin: true //允许全屏最小化
            , anim: 5 //0-6的动画形式，-1不开启
            , content: info.html()
        });
        $.ajax({
            url: "user",
            type: "get",
            dataType: "json",
            success: function (data) {
                if (data.statusCode == 0) {
                    $("#gender").val(data.data.gender);
                    $("#username").val(data.data.username);
                    $("#phone").val(data.data.phone);
                    $("#fileid").val(data.data.avatar);

                    $('#gender').selectize({
                        options: [{
                            name: "男", id: 1
                        }, {
                            name: "女", id: 2
                        }],
                        valueField: 'name',
                        labelField: 'name',
                        create: false,
                        maxItem: 1
                    });
                    if (data.data.avatar != null) {
                        $("#file").fileinput({
                            uploadUrl: "file",
                            uploadAsync: true,
                            maxFileCount: 1,
                            showUpload: true,
                            showRemove: false,
                            showPreview: true,
                            language: 'zh',
                            allowedPreviewTypes: ['image'],
                            allowedFileExtensions: ['jpg', 'png', 'gif'],
                            maxFileSize: 2000,
                            showBrowse: false,
                            browseOnZoneClick: true,
                            initialPreviewAsData: true,
                            initialPreviewFileType: 'image',
                            initialPreview: ["file/" + data.data.avatar]
                        });
                    } else {
                        $("#file").fileinput({
                            uploadUrl: "file",
                            uploadAsync: true,
                            maxFileCount: 1,
                            showUpload: true,
                            showRemove: false,
                            showPreview: true,
                            language: 'zh',
                            allowedPreviewTypes: ['image'],
                            allowedFileExtensions: ['jpg', 'png', 'gif'],
                            maxFileSize: 2000,
                            showBrowse: false,
                            browseOnZoneClick: true,
                        });
                    }
                    $('#file').on('fileuploaded', function (event, data, previewId, index) {
                        if (data.response.statusCode == 0) {
                            $("#fileid").val(data.response.data.id);
                        }
                    });
                    $('#file').on('fileclear', function (event) {
                        $("#fileid").val("");
                    });
                    $("#submit").on("click", function () {
                        $.ajax({
                            url: "user",
                            data: {
                                "username": $("#username").val(),
                                "gender": $('#gender').selectize()[0].selectize.items[0],
                                "avatar": $('#fileid').val(),
                                "phone": $('#phone').val()
                            },
                            type: "put",
                            dataType: "json",
                            success: function (data) {
                                if (data.statusCode == 0) {
                                    layer.tips('修改成功', '#submit', {
                                        tips: [1, '#3595CC'],
                                        time: 2000
                                    });
                                }
                            }
                        });
                    })
                }
            }
        });
    })
    //endregion
    //region 好友分组


    var groups = function () {
        var group = function (groupname) {
            var html = '<div class="box-header with-border">\
        <h3 class="box-title">' + groupname + '</h3>\
            <div class="box-tools pull-right">\
            <button type="button" class="btn btn-box-tool" data-widget="collapse"><i\
        class="fa fa-minus"></i>\
            </button>\
            </div>\
        </div>'
            return html;
        }
        var friends = function (friends) {
            var combine = '';
            for (var i = 0; i < friends.length; i++) {
                var html = ' <div class="user-block list-group-item"  style="margin-bottom: 10px;margin-top: 10px;">\
            <img alt="头像"  class="img-circle img-bordered-sm" src="file/' + friends[i].avatar + '">\
            <span class="username">\
            <a href="javascript:;" class="userid" onclick="friendClick(this)" data-id="' + friends[i].friendid + '">' + friends[i].username + '</a>\
        <a class="pull-right btn-box-tool" href="javascript:;" onclick="deleteFriend(\'' + friends[i].friendid + '\')"><i class="fa fa-times"></i></a>\
            </span></div>'
                combine += html;
            }
            var body = '<div class="box-body">' + combine + '</div>'
            return body;
        }
        $.ajax({
            url: "user/friends",
            type: "get",
            dataType: "json",
            success: function (data) {
                if (data.statusCode == 0) {
                    var child = [];
                    for (var g in data.data) {
                        var html = '<div class="box box-default " id="' + g + '"  style="margin-bottom: 0px;">' + group(g) + friends(data.data[g]) + "</div>";
                        $("#friendpanel").append(html);
                        var o = {};
                        o.title = g;
                        o.cmd = "mov";
                        child.push(o);
                    }
                    $(document).contextmenu({
                        delegate: ".user-block",
                        autoFocus: true,
                        preventContextMenuForPopup: true,
                        preventSelect: true,
                        taphold: true,
                        menu: [{
                            title: "移动到...",
                            uiIcon: "ui-icon-scissors",
                            children: child
                        },
                            {
                                title: "创建分组",
                                cmd: "craete",
                                uiIcon: "ui-icon-copy"
                            }
                        ],
                        // Handle menu selection to implement a fake-clipboard
                        select: function (event, ui) {
                            var $target = ui.target;
                            switch (ui.cmd) {
                                case "mov":
                                    $.ajax({
                                        url: "user/group",
                                        type: "PUT",
                                        data: {
                                            friendid: $(ui.target[0]).find(".userid").attr("data-id"),
                                            group: ui.item.text(),
                                            username: $(ui.target[0]).find(".userid").text(),
                                            avatar: $(ui.target[0]).parent(".user-block").find("img").attr("src").split("/")[1]
                                        },
                                        dataType: "json",
                                        success: function (data) {
                                            if (data.statusCode == 0) {
                                                window.location.href = window.location.href;
                                            }
                                        }
                                    })
                                    break
                                case "craete":
                                    layer.prompt({title: '分组名称', formType: 1}, function (pass, index) {
                                        layer.close(index);
                                        $.ajax({
                                            url: "user/group",
                                            type: "POST",
                                            data: {
                                                group: pass,
                                            },
                                            dataType: "json",
                                            success: function (data) {
                                                if (data.statusCode == 0) {
                                                    window.location.href = window.location.href;
                                                } else {
                                                    layer.msg(data.message);
                                                }
                                            }
                                        })

                                    });
                                    break
                            }
                            console.log("select " + ui.cmd + " on " + $target);

                            // Optionally return false, to prevent closing the menu now
                        }
                        ,
                    })

                }
            }
        })
    }
    groups();
    //endregion
    //region 搜索好友
    $("#savebutten button").on("click", function () {
        layer.open({
            type: 1 //Page层类型
            , area: ['500px', '300px']
            , title: '添加好友'
            , shade: 0.0 //遮罩透明度
            , maxmin: true //允许全屏最小化
            , anim: 5 //0-6的动画形式，-1不开启
            , content: searchpanel.html()
        });
        $("#friendsgroup button").on("click", function () {
            $.ajax({
                url: "user/search?keyword=" + $("#keyword").val(),
                type: "get",
                dataType: "json",
                success: function (data) {
                    if (data.statusCode == 0) {
                        for (var i = 0; i < data.data.length; i++) {
                            $("#friendsgroup").find("table tbody").empty();
                            $("#friendsgroup").find("table tbody").append('<tr>\
                            <td>' + data.data[i].name + '</td>\
                             <td>' + data.data[i].phone + '</td>\
                             <td>' + data.data[i].gender + '</td>\
                              <td><a class="btn btn-primary"  onclick="requestFriend(' + data.data[i].id + ')">添加好友</a></td>\
                                </tr>');
                        }
                    }
                }
            })
        })
    })
    //endregion
    //region消息盒子
    //todo
    var i = setInterval(function () {
        getMsg();
    }, 3000);
    $("#notification").on("click", function () {

        $.ajax({
            url: "msg/getrequest",
            type: "get",
            dataType: "json",
            success: function (data) {
                if (data.statusCode == 0) {
                    for (var i = 0; i < data.data.length; i++) {
                        $("#msgpanel").find("table tbody").empty();
                        if (data.data[i].type == "好友请求") {
                            $("#msgpanel").find("table tbody").append('<tr>\
                            <td>' + data.data[i].username + '</td>\
                             <td>' + data.data[i].type + '</td>\
                             <td>' + data.data[i].msg + '</td>\
                              <td><a class="btn btn-primary"  onclick="addFriend(' + data.data[i].fromwho + ',' + data.data[i].id + ')">确认</a></td>\
                                </tr>');
                        } else {
                            $("#msgpanel").find("table tbody").append('<tr>\
                            <td>' + data.data[i].username + '</td>\
                             <td>' + data.data[i].type + '</td>\
                             <td>' + data.data[i].msg + '</td>\
                              <td><a class="btn btn-primary"  onclick="chat(' + data.data[i].fromwho + ')">聊天</a></td>\
                                </tr>');
                        }


                    }
                    layer.open({
                        type: 1 //Page层类型
                        , area: ['500px', '300px']
                        , title: '信息盒子'
                        , shade: 0.0 //遮罩透明度
                        , anim: 5 //0-6的动画形式，-1不开启
                        , content: $("#msgpanel").html()
                    });
                }
            }
        })
    })

    //endregion
    //region 评论
    var j = function () {
        var html = function (jj) {
            var h = ' <div class="post clearfix">\
                <div class="user-block">\
                <img class="img-circle img-bordered-sm" src="file/' + jj.avatar + '"\
            alt="User Image">\
                <span class="username">\
                <a href="#">' + jj.usename + '</a>\
            </span>\
            <span class="description">' + jj.createtime + '</span>\
            </div>\
            <!-- /.user-block -->\
            <p>\
           ' + jj.note + '\
            </p>\
            </div>'
            return h;
        }

        $.ajax({
            url: "note",
            type: "get",
            dataType: "json",
            success: function (data) {
                if (data.statusCode == 0) {
                    for (var i = 0; i < data.data.length; i++) {
                        $("#settings").append(html(data.data[i]));
                    }
                }
            }
        })
    }
    j();
    //endregion
    //region 动态
    var t = function () {
        var html = function (jj) {
            var h = ' <div class="post clearfix">\
                <div class="user-block">\
                <img class="img-circle img-bordered-sm" src="file/' + jj.avatar + '"\
            alt="User Image">\
                <span class="username">\
                <a href="#">' + jj.username + '</a>\
            </span>\
            <span class="description">' + jj.createtime + '</span>\
            </div>\
            <!-- /.user-block -->\
            <p>\
           ' + jj.note + '\
            </p>\
            </div>'
            return h;
        }

        $.ajax({
            url: "trend",
            type: "get",
            dataType: "json",
            success: function (data) {
                if (data.statusCode == 0) {
                    for (var i = 0; i < data.data.length; i++) {
                        $("#trend").append(html(data.data[i]));
                    }
                }
            }
        })
    }
    t();

    $("#trendpop").on("click", function () {
        layer.open({
            type: 1 //Page层类型
            , area: ['300px', '300px']
            , title: '动态'
            , shade: 0.0 //遮罩透明度
            , anim: 5 //0-6的动画形式，-1不开启
            , content: trend.html()
        });
        $("#trendconfirm").on("click", function () {
            $.ajax({
                url: "trend",
                type: "post",
                data: {
                    note: $("#tre").val()
                },
                dataType: "json",
                success: function (data) {
                    if (data.statusCode == 0) {
                        layer.msg(data.message);
                    }
                }
            })
        })
    })
    //endregion

    //region 群聊

    $("#groupmsg").on("click", function () {
        groupchat();
    })
    //endregion
})


//region 好友信息

function friendClick(e) {
    $.ajax({
        url: "user/" + $(e).attr("data-id"),
        type: "get",
        dataType: "json",
        success: function (data) {
            if (data.statusCode == 0) {
                layer.open({
                    type: 1 //Page层类型
                    , area: ['300px', '400px']
                    , title: '好友信息'
                    , shade: 0.0 //遮罩透明度
                    , anim: 5 //0-6的动画形式，-1不开启
                    , content: usepanel.html()
                });
                $("#uname").val(data.data.username);
                $("#gd").val(data.data.gender);
                $("#ph").val(data.data.phone);

                $("#startChat").on("click", function () {
                    chat(data.data.userid);
                })

                $("#comment").on("click", function () {
                    layer.open({
                        type: 1 //Page层类型
                        , area: ['300px', '300px']
                        , title: '评价'
                        , shade: 0.0 //遮罩透明度
                        , anim: 5 //0-6的动画形式，-1不开启
                        , content: judge.html()
                    });
                    $("#jugconfirm").on("click", function () {
                        $.ajax({
                            url: "note",
                            type: "post",
                            data: {
                                towho: $(e).attr("data-id"),
                                note: $("#jug").val()
                            },
                            dataType: "json",
                            success: function (data) {
                                if (data.statusCode == 0) {
                                    layer.msg(data.message);
                                }
                            }
                        })
                    })
                })

                $("#showcomment").on("click", function () {
                    var html = function (jj) {
                        var h = ' <div class="post clearfix" style="margin: 10px;border: 1px solid #ddd">\
                <div class="user-block">\
                <img class="img-circle img-bordered-sm" src="file/' + jj.avatar + '"\
            alt="User Image">\
                <span class="username">\
                <a href="#">' + jj.usename + '</a>\
            </span>\
            <span class="description">' + jj.createtime + '</span>\
            </div>\
            <!-- /.user-block -->\
            <p>\
           ' + jj.note + '\
            </p>\
            </div>'
                        return h;
                    }
                    var ht = '';
                    $.ajax({
                        url: "note/" + $(e).attr("data-id"),
                        type: "get",
                        dataType: "json",
                        success: function (data) {
                            if (data.statusCode == 0) {
                                for (var i = 0; i < data.data.length; i++) {
                                    ht += html(data.data[i]);
                                }
                                layer.open({
                                    type: 1 //Page层类型
                                    , area: ['600px', '600px']
                                    , title: 'TA的评价'
                                    , shade: 0.0 //遮罩透明度
                                    , anim: 5 //0-6的动画形式，-1不开启
                                    , content: ht
                                });
                            }
                        }
                    })

                })
            }
        }
    })
}

//endregion

function deleteFriend(id) {
    $.ajax({
        url: "user/" + id,
        type: "delete",
        dataType: "json",
        success: function (data) {
            if (data.statusCode == 0) {
                window.location.href = window.location.href;
            } else {
                layer.msg(data.message);
            }
        }
    })
}
function addFriend(id, msgid) {
    $.ajax({
        url: "msg/read/" + msgid,
        type: "put",
        dataType: "json",
        success: function (data) {
        }

    })
    layer.open({
        type: 1 //Page层类型
        , area: ['200px', '200px']
        , title: '选择分组'
        , shade: 0.0 //遮罩透明度
        , anim: 5 //0-6的动画形式，-1不开启
        , content: addpanel.html()
    });
    $.ajax({
        url: "user/groups",
        type: "get",
        dataType: "json",
        success: function (data) {
            if (data.statusCode == 0) {

                var op = [];
                for (var i = 0; i < data.data.length; i++) {
                    op.push({
                        title: data.data[i],
                        id: i
                    })
                }
                var select = $('#groups').selectize({
                    create: false,
                    maxItem: 1,
                    valueField: 'title',
                    labelField: 'title',
                    options: op
                });

            } else {
                layer.msg(data.message);
            }
        }

    })
    $("#groupconfirm").on("click", function () {
        $.ajax({
            url: "user/addfriend",
            type: "post",
            data: {
                friendid: id,
                group: $('#groups').selectize()[0].selectize.items[0]
            },
            dataType: "json",
            success: function (data) {
                if (data.statusCode == 0) {
                    layer.msg(data.message);
                } else {
                    layer.msg(data.message);
                }
            }

        })
    })
}
function requestFriend(id, group) {
    layer.open({
        type: 1 //Page层类型
        , area: ['300px', '200px']
        , title: '请求信息'
        , shade: 0.0 //遮罩透明度
        , anim: 5 //0-6的动画形式，-1不开启
        , content: reqestFriend.html()
    });
    $("#requestbutton").on("click", function () {
        $.ajax({
            url: "user/requestfriend",
            type: "post",
            data: {
                towho: id,
                msg: $("#request").val()
            },
            dataType: "json",
            success: function (data) {
                if (data.statusCode == 0) {
                    layer.msg(data.message);
                } else {
                    layer.msg(data.message);
                }
            }
        })
    })
}

function getMsg() {
    $.ajax({
        url: "msg/getrequest",
        type: "get",
        dataType: "json",
        success: function (data) {
            if (data.statusCode == 0) {
                $("#notification").find("span").text(data.data.length);
            }
        }
    })
}

function chat(id) {
    var makeReciveMsg = function (msg) {
        var recive = ' <div class="direct-chat-msg" data-id="' + msg.id + '">\
           <div class="direct-chat-info clearfix">\
           <span class="direct-chat-name pull-left">' + msg.username + '</span>\
       <span class="direct-chat-timestamp pull-right">' + msg.createtime + '</span>\
       </div>\
       <!-- /.direct-chat-info -->\
       <img class="direct-chat-img" src="file/' + msg.avatar + '"\
       alt="Message User Image"><!-- /.direct-chat-img -->\
       <div class="direct-chat-text">\
           ' + msg.msg + '\
           </div>\
           <!-- /.direct-chat-text -->\
       </div>'
        return recive;
    }
    var makeSendMsg = function (msg) {
        var ms = '<div class="direct-chat-msg right" data-id="' + msg.id + '">\
            <div class="direct-chat-info clearfix">\
            <span class="direct-chat-name pull-right">' + msg.username + '</span>\
        <span class="direct-chat-timestamp pull-left">'
            + msg.createtime +
            '</span>\
                    </div>\
                    <!-- /.direct-chat-info -->\
                    <img class="direct-chat-img" src="file/' + msg.avatar + '" alt="Message User Image"><!-- /.direct-chat-img -->\
        <div class="direct-chat-text">\
              ' + msg.msg + '\
        </div>\
        <!-- /.direct-chat-text -->\
        </div>'
        return ms;
    }
    $.ajax({
        url: "msg/user/" + id,
        type: "get",
        dataType: "json",
        success: function (data) {
            if (data.statusCode == 0) {
                var ii;
                layer.open({
                    type: 1 //Page层类型
                    , area: ['300px', '400px']
                    , title: '聊天窗口'
                    , shade: 0.0 //遮罩透明度
                    , anim: 5 //0-6的动画形式，-1不开启
                    , maxmin: true
                    , content: conve.html(),
                    cancel: function (index, layero) {
                        clearInterval(ii);
                    }
                });
                for (var i = 0; i < data.data.chat.length; i++) {
                    if (data.data.chat[i].fromwho == data.data.current) {
                        $("#allmsg").append(makeSendMsg(data.data.chat[i]));
                    } else {
                        $("#allmsg").append(makeReciveMsg(data.data.chat[i]));
                    }
                }
                //todo
                ii = setInterval(function () {
                    $.ajax({
                        url: "msg/new/" + id + "?time=" + moment().format('YYYY-MM-DD HH:mm:ss'),
                        type: "get",
                        dataType: "json",
                        success: function (data) {
                            if (data.statusCode == 0) {
                                for (var i = 0; i < data.data.chat.length; i++) {
                                    if (data.data.chat[i].fromwho == data.data.current) {
                                        $("#allmsg").append(makeSendMsg(data.data.chat[i]));
                                    } else {
                                        $("#allmsg").append(makeReciveMsg(data.data.chat[i]));
                                    }
                                }
                            }
                        }

                    })
                }, 2000)
                $("#sendbutton").on("click", function () {
                    if ($("#sendmsg").val() != "") {
                        $.ajax({
                            url: "msg",
                            type: "post",
                            data: {
                                msg: $("#sendmsg").val(),
                                towho: id
                            },
                            dataType: "json",
                            success: function (data) {
                                if (data.statusCode == 0) {
                                    $("#allmsg").append(makeSendMsg(data.data));
                                    $("#sendmsg").val("")
                                }

                            }
                        })
                    }

                })
                $("#download").on("click", function () {
                    window.open("msg/downlload/" + id);
                })

                $(document).contextmenu2({
                    addClass: "llay",
                    delegate: ".direct-chat-msg",
                    autoFocus: true,
                    preventContextMenuForPopup: true,
                    preventSelect: true,
                    taphold: true,
                    menu: [{
                        title: "删除消息",
                        cmd: "delete",
                        uiIcon: "ui-icon-scissors"
                    }],
                    select: function (event, ui) {
                        var $target = ui.target;
                        switch (ui.cmd) {
                            case "delete":
                                $.ajax({
                                    url: "msg/" + $(ui.target[0]).closest(".direct-chat-msg").attr("data-id"),
                                    type: "delete",
                                    dataType: "json",
                                    success: function (data) {
                                        if (data.statusCode == 0) {
                                            $(ui.target[0]).closest(".direct-chat-msg").remove();
                                        }
                                    }
                                })
                                break

                        }
                        console.log("select " + ui.cmd + " on " + $target);

                    }

                })

            }
        }

    })

}

function groupchat() {
    var makeReciveMsg = function (msg) {
        var recive = ' <div class="direct-chat-msg" data-id="' + msg.id + '">\
           <div class="direct-chat-info clearfix">\
           <span class="direct-chat-name pull-left">' + msg.username + '</span>\
       <span class="direct-chat-timestamp pull-right">' + msg.createtime + '</span>\
       </div>\
       <!-- /.direct-chat-info -->\
       <img class="direct-chat-img" src="file/' + msg.avatar + '"\
       alt="Message User Image"><!-- /.direct-chat-img -->\
       <div class="direct-chat-text">\
           ' + msg.msg + '\
           </div>\
           <!-- /.direct-chat-text -->\
       </div>'
        return recive;
    }
    var makeSendMsg = function (msg) {
        var ms = '<div class="direct-chat-msg right" data-id="' + msg.id + '">\
            <div class="direct-chat-info clearfix">\
            <span class="direct-chat-name pull-right">' + msg.username + '</span>\
        <span class="direct-chat-timestamp pull-left">'
            + msg.createtime +
            '</span>\
                    </div>\
                    <!-- /.direct-chat-info -->\
                    <img class="direct-chat-img" src="file/' + msg.avatar + '" alt="Message User Image"><!-- /.direct-chat-img -->\
        <div class="direct-chat-text">\
              ' + msg.msg + '\
        </div>\
        <!-- /.direct-chat-text -->\
        </div>'
        return ms;
    }
    $.ajax({
        url: "gmsg/user",
        type: "get",
        dataType: "json",
        success: function (data) {
            if (data.statusCode == 0) {
                var ii;
                layer.open({
                    type: 1 //Page层类型
                    , area: ['300px', '400px']
                    , title: '聊天窗口'
                    , shade: 0.0 //遮罩透明度
                    , anim: 5 //0-6的动画形式，-1不开启
                    , maxmin: true
                    , content: conve.html(),
                    cancel: function (index, layero) {
                        clearInterval(ii);
                    }
                });
                for (var i = 0; i < data.data.chat.length; i++) {
                    if (data.data.chat[i].fromwho == data.data.current) {
                        $("#allmsg").append(makeSendMsg(data.data.chat[i]));
                    } else {
                        $("#allmsg").append(makeReciveMsg(data.data.chat[i]));
                    }
                }
                //todo
                ii = setInterval(function () {
                    $.ajax({
                        url: "gmsg/new?time=" + moment().format('YYYY-MM-DD HH:mm:ss'),
                        type: "get",
                        dataType: "json",
                        success: function (data) {
                            if (data.statusCode == 0) {
                                for (var i = 0; i < data.data.chat.length; i++) {
                                    if (data.data.chat[i].fromwho == data.data.current) {
                                        $("#allmsg").append(makeSendMsg(data.data.chat[i]));
                                    } else {
                                        $("#allmsg").append(makeReciveMsg(data.data.chat[i]));
                                    }
                                }
                            }
                        }

                    })
                }, 2000)
                $("#sendbutton").on("click", function () {
                    if ($("#sendmsg").val() != "") {
                        $.ajax({
                            url: "gmsg",
                            type: "post",
                            data: {
                                msg: $("#sendmsg").val(),
                            },
                            dataType: "json",
                            success: function (data) {
                                if (data.statusCode == 0) {
                                    $("#allmsg").append(makeSendMsg(data.data));
                                    $("#sendmsg").val("")
                                }

                            }
                        })
                    }

                })
                $("#download").on("click", function () {
                    window.open("gmsg/downlload");
                })

                $(document).contextmenu2({
                    addClass: "llay",
                    delegate: ".direct-chat-msg",
                    autoFocus: true,
                    preventContextMenuForPopup: true,
                    preventSelect: true,
                    taphold: true,
                    menu: [{
                        title: "删除消息",
                        cmd: "delete",
                        uiIcon: "ui-icon-scissors"
                    }],
                    select: function (event, ui) {
                        var $target = ui.target;
                        switch (ui.cmd) {
                            case "delete":
                                $.ajax({
                                    url: "gmsg/" + $(ui.target[0]).closest(".direct-chat-msg").attr("data-id"),
                                    type: "delete",
                                    dataType: "json",
                                    success: function (data) {
                                        if (data.statusCode == 0) {
                                            $(ui.target[0]).closest(".direct-chat-msg").remove();
                                        }
                                    }
                                })
                                break

                        }
                        console.log("select " + ui.cmd + " on " + $target);

                    }

                })

            }
        }

    })

}