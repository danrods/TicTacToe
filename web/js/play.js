/**
 * Created by drodrigues on 2/1/16.
 */



var isWin = false;
var aiThinking = false;

var timer = setInterval(function(){countDown();}, 1000);

$(document).ready(function(){



    $('#modalTitle').text('Get Ready to Play!');
    $('#modalFooter').hide();
    $('#winModal').modal("show");
    $(".alert").hide();

    $(".cellBlock img").each(function(){
        $(this).click(function(event){
            event.preventDefault();
            var $this = $(this);
            if( (!isWin) && (!aiThinking)){
                $.ajax({
                    type: "GET",
                    url: "/play/makeMove/",
                    data: {selectedBlock: $this.attr('id')},
                    success: function(data){

                        $this.attr('src', data.img);

                        if(data.win === 1){
                            $('#modalTitle').text("Congratulations!");
                            $('#modalText').text("You Won!!");
                            $('#winModal').modal("show");
                            isWin = true;
                        }
                        else if(data.win === 2){
                            $('#modalTitle').text('Game Over');
                            $('#modalText').text('You Tied!!');
                            $('#winModal').modal("show");
                            this.isWin = true;
                        }
                        else{
                            findAIMove();
                        }


                    },
                    error: function(error){
                        if(error.err === "taken"){
                            $('#errMsg').text("The selected spot is already taken");
                        }
                        else if(error.err === "exception"){
                            $('#errMsg').text("There was an error communicating with the server");
                        }
                        else{
                            $('#errMsg').text("Something went wrong");
                        }
                        $(".alert").show();
                        setTimeout(function(){
                            $(".alert").hide();
                        }, 3000);
                    },
                    dataType:"json"
                });
            }



        });

    });


});

function findAIMove(){

    this.aiThinking = true;

    $("#popover").popover("show");
    $.ajax({
        type: "GET",
        url: "/play/findMove/",
        success: function(data){

            var id = "#" + data.id;
            $(id).attr('src', data.img);

            if(data.win === 1){
                var title = $('#modalTitle');
                $('#modalTitle').text('Sorry!!');
                $('#modalText').text('You Lost!!');
                $('#winModal').modal("show");
                this.isWin = true;
            }
            else if(data.win === 2){
                $('#modalTitle').text('Game Over');
                $('#modalText').text('You Tied!!');
                $('#winModal').modal("show");
                this.isWin = true;
            }


        },
        error: function(err){
            if(error.err === "exception"){
                $('#errMsg').text("There was an error communicating with the server");
            }
            else{
                $('#errMsg').text("Something went wrong");
            }
            $(".alert").show();
            setTimeout(function(){
                $(".alert").hide();
            }, 3000);
        },
        dataType:"json"
    });


    this.aiThinking = false;
    $("#popover").popover("hide");
    $("#playerpop").popover("show");
    setTimeout(function(){
        $("#playerpop").popover("hide");
    }, 2500);
}



var countdown = 5;
function countDown(){
    console.log("Counting down...")
    if(countdown > 0){
        $('#modalText').text('' + countdown);
        countdown = countdown - 1;
    }
    else{
        $('#winModal').modal("hide");
        $('#modalFooter').show();
        clearInterval(this.timer);
    }
}

