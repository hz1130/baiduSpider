
function hookTest1_3() {
    Java.perform(function () {
        var utils = Java.use("com.baidu.homework.base.p");
        utils.d.overload().implementation = function () {
            console.log("ab ------ info .....");
            retval = this.d();
            return retval;
        }
    })
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

function hook_append() {
    Java.perform(function () {
        var utils = Java.use("java.lang.StringBuilder");
        utils.append.overload('java.lang.String').implementation = function (str) {
            var retval = this.append(str);
            if(str.indexOf("cuid")!=-1){
                console.log("str : ", str);
                console.log("retval : ", retval);
                printStacks();
            }

            return retval;
        }
    })
}


function hookAllMethod() {
    Java.perform(function () {
        Java.enumerateClassLoaders({
            onMatch: function (loader) {
                try {
                    if (loader.findClass("com.baidu.searchbox.godeye.callback.BDBoxCallbackImpl")) {
                        // console.log(loader);
                        Java.classFactory.loader = loader
                        var md5 = Java.use("com.baidu.graph.network.BaseRequest");
                        var methods = md5.class.getDeclaredMethods();
                        // 获取所有方法
                        for(var j = 0; j < methods.length; j++){
                            var methodName = methods[j].getName();
                            console.log(methodName);
                
                            // 获取重载
                            for(var k = 0; k < md5[methodName].overloads.length; k++){
                
                                md5[methodName].overloads[k].implementation = function(){
                                    for(var i = 0; i < arguments.length; i++){
                                        console.log(arguments[i]);
                                    }
                                    return this[methodName].apply(this, arguments);
                                }
                            }
                
                        }
                    }
                } catch (ignore) {
                    // console.log(ignore.toString());
                }
            },
            onComplete: function () {
                console.log(" search completed!")
            }
        });

    });

}



function hook_TokenUitls() {
    Java.perform(function () {
        Java.enumerateClassLoaders({
            onMatch: function (loader) {
                try {
                    if (loader.findClass("com.baidu.searchbox.godeye.callback.BDBoxCallbackImpl")) {
                        // console.log(loader);
                        Java.classFactory.loader = loader
                        Java.use("com.baidu.searchbox.godeye.callback.BDBoxCallbackImpl").extractImgBase64Feature.implementation = function (img) {
                            console.log("================= info Search ================");
                            // printStacks("hook_TokenUitls");
                            console.log("img : ", img)
                            var token = this.extractImgBase64Feature(img)
                            console.log(token);
                            return token
                        }
                    }
                } catch (ignore) {
                    // console.log(ignore.toString());
                }
            },
            onComplete: function () {
                console.log(" search completed!")
            }
        });

    });

}


function hook_creatToken() {
    Java.perform(function () {
        Java.enumerateClassLoaders({
            onMatch: function (loader) {
                try {
                    if (loader.findClass("com.baidu.searchbox.godeye.callback.BDBoxCallbackImpl")) {
                        // console.log(loader);
                        Java.classFactory.loader = loader
                        Java.use("com.baidu.searchbox.godeye.utils.TokenUtils").createToken.implementation = function (img, time, z, interfaceName) {
                            console.log("================= info Search ================");
                            // printStacks("hook_TokenUitls");
                            console.log("img : ", img)
                            console.log("time : ", time)
                            console.log("interfaceName : ", interfaceName)
                            var token = this.createToken(img, time, z, interfaceName)
                            console.log(token);
                            return token
                        }

                    }
                } catch (ignore) {
                    // console.log(ignore.toString());
                }
            },
            onComplete: function () {
                console.log(" search completed!")
            }
        });

    });

}



function hook_getParam() {
    Java.perform(function () {
        Java.enumerateClassLoaders({
            onMatch: function (loader) {
                try {
                    if (loader.findClass("com.baidu.graph.sdk.data.requests.OcrDirectRequest")) {
                        // console.log(loader);
                        Java.classFactory.loader = loader
                        Java.use("com.baidu.graph.sdk.data.requests.OcrDirectRequest").getParam.implementation = function () {
                            console.log("================= info Search ================");
                            console.log(this.getParam());
                            return this.getParam()
                        }
                    }
                } catch (ignore) {
                    // console.log(ignore.toString());
                }
            },
            onComplete: function () {
                console.log(" search completed!")
            }
        });

    });

}


function hook_Sign() {
    Java.perform(function () {
        Java.enumerateClassLoaders({
            onMatch: function (loader) {
                try {
                    if (loader.findClass("com.baidu.searchbox.godeye.utils.TokenUtils")) {
                        console.log(loader);
                        Java.classFactory.loader = loader
                        Java.use("com.baidu.graph.utils.encrypt.MD5Utils").toMd5Lower.implementation = function (str1) {
                            console.log("================= info Search ================");
                            // printStacks("hook_TokenUitls");
                            console.log("str : ", str1)
                            var ature = this.toMd5Lower(str1);
                            console.log("ature : ", ature);
                            printStacks();
                            return ature
                        }
                    }
                } catch (ignore) {
                    // console.log(ignore.toString());
                }
            },
            onComplete: function () {
                console.log(" search completed!")
            }
        });

    });

}

function hook_RSA() {
    Java.perform(function () {
        Java.enumerateClassLoaders({
            onMatch: function (loader) {
                try {
                    if (loader.findClass("com.baidu.searchbox.godeye.utils.TokenUtils")) {
                        console.log(loader);
                        Java.classFactory.loader = loader
                        Java.use("com.baidu.graph.sdk.utils.RSA").encryptStr.implementation = function (str1) {
                            console.log("================= info Search ================");
                            // printStacks("hook_TokenUitls");
                            console.log("str : ", str1)
                            var ature = this.encryptStr(str1);
                            console.log("ature : ", ature);
                            return ature
                        }
                    }
                } catch (ignore) {
                    // console.log(ignore.toString());
                }
            },
            onComplete: function () {
                console.log(" search completed!")
            }
        });

    });

}


function hook_generateImageKey() {
    Java.perform(function () {
        Java.enumerateClassLoaders({
            onMatch: function (loader) {
                try {
                    if (loader.findClass("com.baidu.searchbox.godeye.utils.TokenUtils")) {
                        console.log(loader);
                        Java.classFactory.loader = loader
                        Java.use("com.baidu.graph.libcore.utils.Utility").generateImageKey.implementation = function (str1) {
                            console.log("================= info Search ================");
                            // printStacks("hook_TokenUitls");
                            console.log("str : ", str1)
                            var imgkey = this.generateImageKey(str1);
                            console.log(imgkey);
                            return imgkey;
                        }
                    }
                } catch (ignore) {
                    // console.log(ignore.toString());
                }
            },
            onComplete: function () {
                console.log(" search completed!")
            }
        });

    });

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

function hookDemo() {
    Java.perform(function () {
        var utils = Java.use("com.baidu.http.engine.urlconnect.b.c");
        utils.a.overload('com.baidu.searchbox.network.outback.core.Request').implementation = function (Request) {
            console.log("Request >>>>> \t\n", Request);
            printStacks();
            var retval = this.a(Request);
            console.log("retval >>>>> \t\n", retval);
            return retval;
        }
    })
}

function hookBDUSS() {
    Java.perform(function () {
        var utils = Java.use("com.baidu.http.engine.urlconnect.b.a");
        utils.a.overload('com.baidu.http.engine.urlconnect.d$a').implementation = function (connect) {
            console.log("connect >>>>> \t\n", connect);
            printStacks();
            var retval = this.a(connect);
            console.log("retval >>>>> \t\n", retval);
            return retval;
        }
    })
}



function hookUrl() {
    Java.perform(function () {
        var utils = Java.use("com.baidu.searchbox.network.c.a.d");
        utils.a.implementation = function (j, str) {
            // printStacks();
            console.log("j >>>>> \t\n", j);
            console.log("str >>>>> \t\n", str);
            var retval = this.a(j, str);
            console.log("retval >>>>> \t\n", retval);
            return retval;
        }
    })
}


function hook_nativeAddRequestHeader() {
    var soAddr = Module.findBaseAddress("libGraphSearch.so");
    console.log(soAddr);
    var funcAddr = soAddr.add(0x6F48); //函数地址计算 thumb+1 ARM不加
    console.log(funcAddr);

    if (funcAddr != null) {
        Interceptor.attach(funcAddr, {
            onEnter: function (args) {
                console.log(hexdump(args[2]) + "\t\n");
            },
            onLeave: function (retval) {
                console.log("retval : ", retval);
            }
        });
    }
}





function main() {
    hook_toString();
    // hook_toTimes();
    // hook_TokenUitls();
    // hook_getParam();
    // hook_TokenUitls();
    // hook_RSA();
    // hook_generateImageKey();

    // hook_TokenUitls();
    // hook_creatToken();
    // hook_Sign();
    // hookAllMethod();

    // hook_nativeAddRequestHeader();
    // hook_append();
    // hook_getSdkSign();
}

setImmediate(main);