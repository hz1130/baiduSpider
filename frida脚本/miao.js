function hookSmantiDeviceId() {
    Java.perform(function () {
        var utils = Java.use("com.ishumei.smantifraud.SmAntiFraud");
        utils.getDeviceId.implementation = function () {
            console.log("ab ------ info .....");
            var retval = this.getDeviceId();
            console.log(retval)
            return retval;
        }
    })
}

function hookChoose(){
    Java.perform(function(){
        var list = null
        Java.choose("com.ishumei.O0000O000000oO.O00O0000OooO", {

            onMatch: function(obj){
                list =  obj.O00O0000o00O.value; //字段名与函数名相同 前面加个下划线
                // console.log(list.toString());
            },
            onComplete: function(){
                // console.log("complete : " , list )
            }
        });
    });
}

function hook_toTimes() {
    Java.perform(function () {
        var utils = Java.use("java.lang.System");
        utils.currentTimeMillis.implementation = function () {
            var retval = this.currentTimeMillis();
            printStacks();
            console.log(retval);
            return retval;
        }
    })
}

function hookSmanti() {
    Java.perform(function () {
        var utils = Java.use("com.ishumei.O0000O000000oO.O00O0000OooO");
        utils.getDeviceId.implementation = function () {
            console.log("ab ------ info .....");
            var retval = this.getDeviceId();
            console.log(retval)
            return retval;
        }
    })
}




function hook_overloads() {
    Java.perform(function () {
        var JavaString = Java.use("java.lang.String");
        // hook 指定方法的所有重载
        var ClassName = Java.use("com.ishumei.O0000O000000oO.O00O0000OooO");
        var overloadsLength = ClassName.O000O00000OoO.overloads.length;
        for (var i = 0; i < overloadsLength; i++) {
            ClassName.O000O00000OoO.overloads[i].implementation = function () {
                // 遍历打印 arguments 
                for (var a = 0; a < arguments.length; a++) {
                    console.log(a + " : " + arguments[a]);
                }
                // 调用原方法
                return this.O000O00000OoO.apply(this, arguments);
            }
        }
    });
}



function hook_append() {
    Java.perform(function () {
        var utils = Java.use("java.lang.StringBuilder");
        utils.append.overload('java.lang.String').implementation = function (str) {
            var retval = this.append(str);
            console.log("str : ", str);
            console.log("retval : ", retval);

            return retval;
        }
    })
}

function printStacks(name) {
    console.log("====== printStacks start ====== " + name + "==============================")

    // sample 1
    var throwable = Java.use("android.util.Log").getStackTraceString(Java.use("java.lang.Throwable").$new());
    console.log(throwable);

    // sample 2
    // var exception = Java.use("android.util.Log").getStackTraceString(Java.use("java.lang.Exception").$new());
    // console.log(exception);

    console.log("====== printStacks end ======== " + name + "==============================")
}


function hook_toString() {
    Java.perform(function () {
        var utils = Java.use("java.lang.String");
        var retval = null;
        utils.toString.implementation = function () {
            retval = this.toString();
            if (retval.indexOf("ab_groups") != -1) {
                console.log(retval);
                printStacks();
            }

            return retval;
        }
    })
}


function main() {
    // hook_toString();
    // hook_toTimes();
    // hook_append();
    hookChoose();
    // hookSmantiDeviceId();
    // hook_overloads();
    // hookSmanti();
}

setImmediate(main);