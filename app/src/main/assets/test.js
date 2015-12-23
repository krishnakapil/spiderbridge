var dataArr = [];

function notifyTouchEvent(viewName){
    dataArr.push('Touched ' + viewName);
    checkData();
}

function notifySwipeEvent(viewName) {
    dataArr.push('Swiped ' + viewName);
    checkData();
}

function notifyClickEvent(viewName) {
    dataArr.push('Clicked ' + viewName);
    checkData();
}

function checkData() {
    if(dataArr.length > 99) {
        var cnt = dataArr.length;
        var eventText = 'Events\n';
        for (i = 0; i < cnt; i++) {
            eventText += dataArr[i];
            if(i != cnt-1) {
                eventText += ", ";
            }
        }

        dataArr = [];

        processEvent(eventText);
    }
}