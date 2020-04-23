$(document).ready(function(){
    setInterval(function () {
        $.ajax({
            async: false,
            url: "/video/remindMessage.do",
            type: "GET",
            datatype:"json",
            success:function(data){
                console.log(data);
                for (var i = 0; i < data.obje.length; i++) {
                    if (data.obje[i].updateName!=null && ""!=data.obje[i].updateName){
                        $('.alert').html("你关注的"+data.obje[i].updateName+"更新了").addClass("alert-success").show().delay(3000).fadeOut();
                    }
                }
            }
        })
    },5000)
})