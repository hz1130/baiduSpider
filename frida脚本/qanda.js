
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
            if (retval.indexOf("Pixel 3") != -1) {
                console.log(retval);
                printStacks();
            }

            return retval;
        }
    })
}


function hookap() {
    Java.perform(function () {
        var utils = Java.use("oe0.o");
        utils.m.implementation = function (str, obj) {

            console.log("str : ", str);
            console.log("obj : ", obj);
            var retval = this.m(str, obj);
            console.log(retval)
            return retval;
        }
    })
}

function hookurl() {
    Java.perform(function () {
        var utils = Java.use("j20.b");
        utils.l.implementation = function (str) {

            console.log("str : ", str);
            var retval = this.l(str);
            console.log("retval : ", retval)
            printStacks();
            return retval;
        }
    })
}

function hookintercept() {
    Java.perform(function () {
        var utils = Java.use("com.mathpresso.qanda.data.network.interceptor.c");
        utils.a.implementation = function (str) {

            console.log("str : ", str);
            var retval = this.a(str);
            console.log("retval : ", retval)
            printStacks();
            return retval;
        }
    })
}

function hookBearer() {
    Java.perform(function () {
        var utils = Java.use("oe0.o");
        utils.m.implementation = function (str, obj) {
            var retval = null;
            if (str.indexOf("Bearer") != -1) {
                printStacks();
                console.log("obj : ", obj);
                console.log("str : ", str);
                retval = this.m(str, obj);
                console.log("retval : ", retval)
            }
            retval = this.m(str, obj);

            return retval;
        }
    })
}


function hookArgsImg_key() {
    Java.perform(function () {
        var utils = Java.use("o10.b");
        utils.A.implementation = function (str, str2, z) {
            var retval = null;
            console.log("str : ", str);
            console.log("str2 : ", str2);
            retval = this.A(str, str2, z);
            console.log("retval : ", retval)
            printStacks();
            return retval;
        }
    })
}








function main() {
    // hookap();
    // hookurl();
    // hook_toString();
    hookBearer();
    // hook_toString();
    // hookintercept();
    // hook_toTimes();
    // hookArgsImg_key();
    // hook_append();
    // hook_overloads();
}

setImmediate(main);