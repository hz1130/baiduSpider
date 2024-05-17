
function hook_toString() {
    Java.perform(function () {
        var utils = Java.use("java.lang.String");
        var retval = null;
        utils.toString.implementation = function () {
            retval = this.toString();
            if (retval.indexOf("BAIDUCUID") != -1) {
                console.log(retval);
                printStacks();
            }

            return retval;
        }
    })
}

function hook_tosubString() {
    Java.perform(function () {
        var utils = Java.use("java.lang.String");
        var retval = null;
        utils.substring.overload('int', 'int').implementation = function (int1, int2) {
            retval = this.substring(int1, int2);
            if (retval == "ga2etYiFvag_iS8Oga2zuluPH8giu28glu2yil8h2a8-9QP14iSMuSHoA") {
                console.log(int1);
                console.log("substring buff2 : ", int2);
                printStacks();
            }

            return retval;
        }
    })
}

// 初步 uid 生成通过 str3 传递。
function hookUid() {
    Java.perform(function () {
        var utils = Java.use("com.baidu.searchbox.util.BaiduIdentityManager");
        utils.a.overload('java.lang.String', 'java.lang.String', 'java.lang.String').implementation = function (str1, str2, str3) {
            var retval = null;
            if (str2 == "uid") {
                console.log(str3);
                printStacks();
            }
            retval = this.a(str1, str2, str3);
            return retval;
        }
    })
}

function hookUid_c() {
    Java.perform(function () {
        var utils = Java.use("com.baidu.searchbox.util.BaiduIdentityManager");
        utils.a.overload('java.lang.String', 'java.lang.String', 'java.lang.String').implementation = function (str1, str2, str3) {
            var retval = null;
            console.log("str1 : ", str1);
            console.log("str2 : ", str2);
            console.log("str3 : ", str3);
            retval = this.a(str1, str2, str3);
            console.log(retval)
            // printStacks();
            return retval;
        }
    })
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

function hook_generateImageKey() {
    Java.perform(function () {
        Java.enumerateClassLoaders({
            onMatch: function (loader) {
                try {
                    if (loader.findClass("com.baidu.searchbox.godeye.utils.TokenUtils")) {
                        console.log(loader);
                        Java.classFactory.loader = loader
                        Java.use("com.baidu.graph.sdk.data.requests.OcrDirectRequest").parseResponseBody.implementation = function (str1) {
                            console.log("================= info Search ================");
                            // printStacks("hook_TokenUitls");
                            console.log("str : ", str1)
                            var imgkey = this.parseResponseBody(str1);
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


function hook_getBaiduCuid() {
    Java.perform(function () {
        Java.enumerateClassLoaders({
            onMatch: function (loader) {
                try {
                    if (loader.findClass("com.baidu.searchbox.godeye.utils.TokenUtils")) {
                        console.log(loader);
                        Java.classFactory.loader = loader
                        Java.use("com.baidu.searchbox.godeye.utils.BDCommonParamsUtils").removeQueryCommonParams.implementation = function (str) {
                            console.log("================= info Search ================");
                            console.log("str1 : ", str);
                            // console.log("str2 : ", str2);
                            // console.log("str3 : ", str3);
                            // printStacks();
                            var retval = this.removeQueryCommonParams(str);
                            console.log("retval : ", retval)
                            return retval;
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

function hook_HzaUid() {
    Java.perform(function () {
        Java.enumerateClassLoaders({
            onMatch: function (loader) {
                try {
                    if (loader.findClass("com.baidu.searchbox.godeye.utils.TokenUtils")) {
                        console.log(loader);
                        Java.classFactory.loader = loader
                        Java.use("com.baidu.graph.network.RequestConfig").commonParam.implementation = function (str) {
                            console.log("================= info Search ================");
                            console.log("str : ", str);
                            // console.log("str2 : ", str2);
                            // console.log("str3 : ", str3);
                            // console.log("str3 : ", str3);
                            // printStacks();
                            var retval = this.commonParam(str);
                            console.log("retval : ", retval)
                            printStacks();
                            return retval;
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

function hook_UtilsKt() {
    Java.perform(function () {
        Java.enumerateClassLoaders({
            onMatch: function (loader) {
                try {
                    if (loader.findClass("com.baidu.searchbox.godeye.utils.TokenUtils")) {
                        console.log(loader);
                        Java.classFactory.loader = loader
                        Java.use("com.baidu.graph.network.util.UtilsKt").processUrl.implementation = function (str) {
                            console.log("================= info Search ================");
                            console.log("str : ", str);
                            // console.log("str2 : ", str2);
                            // console.log("str3 : ", str3);
                            // console.log("str3 : ", str3);
                            // printStacks();
                            var retval = this.processUrl(str);
                            console.log("retval : ", retval)
                            // printStacks();
                            return retval;
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


function hook_getCookieStr() {
    Java.perform(function () {
        Java.enumerateClassLoaders({
            onMatch: function (loader) {
                try {
                    if (loader.findClass("com.baidu.graph.context.GraphContextKt")) {
                        console.log(loader);
                        Java.classFactory.loader = loader
                        Java.use("com.baidu.graph.context.GraphContextKt").getCookieStr.implementation = function (str1, str2, str3, long) {
                            console.log("================= info Search ================");
                            console.log("str1 : ", str1);
                            console.log("str2 : ", str2);
                            console.log("str3 : ", str3);
                            // console.log("str3 : ", str3);
                            // printStacks();
                            var retval = this.getCookieStr(str1, str2, str3, long);
                            console.log("retval : ", retval)
                            // printStacks();
                            return retval;
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

function hook_getInfoParam() {
    Java.perform(function () {
        Java.enumerateClassLoaders({
            onMatch: function (loader) {
                try {
                    if (loader.findClass("com.baidu.graph.log.LogManager")) {
                        console.log(loader);
                        Java.classFactory.loader = loader
                        Java.use("com.baidu.graph.log.LogManager").getInfoParam.implementation = function (str1, str2, str3) {
                            console.log("================= info Search ================");
                            console.log("str1 : ", str1);
                            console.log("str2 : ", str2);
                            console.log("str3 : ", str3);
                            // console.log("str3 : ", str3);
                            // printStacks();
                            var retval = this.getInfoParam(str1, str2, str3);
                            console.log("retval : ", retval)
                            // printStacks();
                            return retval;
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




function hook_getCookie() {
    Java.perform(function () {
        Java.enumerateClassLoaders({
            onMatch: function (loader) {
                try {
                    if (loader.findClass("com.baidu.searchbox.godeye.config.BDBoxHttpConfigImpl")) {
                        console.log(loader);
                        Java.classFactory.loader = loader
                        Java.use("com.baidu.searchbox.godeye.config.BDBoxHttpConfigImpl").getCookie.implementation = function () {
                            console.log("================= info Search ================");

                            // console.log("str3 : ", str3);
                            // printStacks();
                            var retval = this.getCookie();
                            console.log("retval : ", retval)
                            printStacks();
                            return retval;
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

function hook_getHttpConfigStatus() {
    Java.perform(function () {
        Java.enumerateClassLoaders({
            onMatch: function (loader) {
                try {
                    if (loader.findClass("com.baidu.graph.libcore.utils.Utility")) {
                        console.log(loader);
                        Java.classFactory.loader = loader
                        Java.use("com.baidu.graph.libcore.utils.Utility").getHttpConfigStatus.implementation = function (str1, str2, str3) {
                            console.log("================= info Search ================");
                            console.log("str1 : ", str1);
                            console.log("str2 : ", str2);
                            console.log("str3 : ", str3);
                            // printStacks();
                            var retval = this.getHttpConfigStatus(str1, str2, str3);
                            console.log("retval : ", retval)
                            printStacks();
                            return retval;
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


function hook_getUid() {
    Java.perform(function () {
        Java.enumerateClassLoaders({
            onMatch: function (loader) {
                try {
                    if (loader.findClass("com.baidu.searchbox.godeye.config.BDBoxHttpConfigImpl")) {
                        console.log(loader);
                        Java.classFactory.loader = loader
                        Java.use("com.baidu.graph.sdk.AppConfigKt").getUid.implementation = function (content) {
                            console.log("================= info Search ================");

                            // console.log("str3 : ", str3);
                            // printStacks();
                            var retval = this.getUid(content);
                            console.log("retval : ", retval)
                            printStacks();
                            return retval;
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




// function hookBase64Encoder() {
//     Java.perform(function () {
//         var utils = Java.use("com.baidu.util.Base64Encoder");
//         utils.b64Encode.implementation = function (byteaar) {
//             var retval = null;
//             var s = Java.use("java.lang.String");
//             var new_s = s.$new(byteaar)
//             console.log("参数 : ", new_s);
//             retval = this.b64Encode(byteaar);
//             var new_retval = s.$new(retval)
//             console.log("返回值 : ", new_retval)
//             return retval;
//         }
//     })
// }


// function hookBase64Encoder() {
//     Java.perform(function () {
//         var utils = Java.use("com.baidu.util.Base64Encoder");
//         utils.b64Encode.implementation = function (byteaar) {
//             var retval = null;
//             var s = Java.use("java.lang.String");
//             var new_s = s.$new("hqmz9uyvabtl100mmxiq9khpt9aw26r4|VMFRBJDIY").getBytes("UTF-8")
//             console.log(new_s)
//             retval = this.b64Encode(new_s);
//             var new_retval = s.$new(retval)
//             console.log("返回值 : ", new_retval)
//             return retval;
//         }
//     })
// }









// 传入参数转String :  3007751338
// retval转String :  _avjiguKviGedqqqB 

function invoke_Base64Encoder() {
    Java.perform(function () {
        var utils = Java.use("com.baidu.util.Base64Encoder");
        var JavaString = Java.use("java.lang.String");

        var str = "hqmz9uyvabtl100mmxiq9khpt9aw26r4|VMFRBJDIY";

        var result_string = null;
        var strBytes = JavaString.$new(str).getBytes("UTF-8");

        var result = utils.b64Encode(strBytes)
        result_string = JavaString.$new(result);
        console.log("\"" + result_string + "\"" + ",");

    })
}

function hookBase64Encoder() {
    Java.perform(function () {
        var utils = Java.use("com.baidu.util.Base64Encoder");
        var JavaString = Java.use("java.lang.String");
        utils.b64Encode.implementation = function (byteaar) {
            var JavaString = Java.use("java.lang.String");
            var str = "ddhqozlasprllzx5hobh3aygitsx59ld|VQMAFAGRO";
            var strBytes = JavaString.$new(str).getBytes("UTF-8");
            var retval = this.b64Encode(strBytes);
            var new_retval = JavaString.$new(retval)
            console.log("返回值 : ", new_retval)
            return retval;
        }
    })
}




function invoke_Base64Encoder_Plus() {
    Java.perform(function () {
        var utils = Java.use("com.baidu.util.Base64Encoder");
        var JavaString = Java.use("java.lang.String");

        //借用java转成byte类型

        var listArr = [
           "y2qkxxk66tz8rappj1m6tgwltbc9u7on|VPPUQSLQF",
        ]

        var result_string = null;
        for (var i = 0; i < listArr.length; i++) {
            var strBytes = JavaString.$new(listArr[i]).getBytes("UTF-8");

            var result = utils.b64Encode(strBytes)
            result_string = JavaString.$new(result);
            console.log("\"" + result_string + "\"" + ",");
        }


    })
}



function for_Encoder() {
    Java.perform(function () {
        var listArr = ["bbcqcd6h3y30haznlq772ysvq7p2imjk|VUZMGYDDL", "ddhqozlasprllzx5hobh3aygitsx59ld|VQMAFAGRO", "yirvnqqnanm8my3y61x2eskvse9j1uym|VOJLREACG"];

        for (var i = 0; i < listArr.length; i++) {
            console.log(listArr[i])
        }

    });
}


function hookuid_md5() {
    Java.perform(function () {
        var JavaString = Java.use("java.lang.String");
        var utils = Java.use("com.baidu.cesium.l.c");
        utils.a.overload('[B', 'boolean').implementation = function (str, z) {
            var retval = null;
            var strBytes = JavaString.$new(str);
            console.log("str : ", strBytes);
            retval = this.a(str, z);
            console.log(retval)
            printStacks();
            return retval;
        }
    })
}

function hookuid_mod() {
    Java.perform(function () {
        var JavaString = Java.use("java.lang.String");
        var utils = Java.use("com.baidu.cesium.f");
        utils.d.overload('java.lang.String').implementation = function (str) {
            var retval = null;
            console.log("str : ", str);
            retval = this.d(str);
            console.log(retval)
            return retval;
        }
    })
}

function hookuid_ga() {
    Java.perform(function () {
        var JavaString = Java.use("java.lang.String");
        var utils = Java.use("com.baidu.cesium.k.a.g");
        utils.a.implementation = function () {
            var retval = null;
            retval = this.a();
            var ret_news = JavaString.$new(retval);
            console.log(ret_news)
            return retval;
        }
    })
}

function hookuid_c() {
    Java.perform(function () {
        var JavaString = Java.use("java.lang.String");
        var utils = Java.use("com.baidu.cesium.h$a");
        utils.a.overload('java.lang.String').implementation = function (str) {
            var retval = null;
            console.log(str)
            retval = this.a(str);
            console.log(retval)
            return retval;
        }
    })
}

function hook_overloads() {
    Java.perform(function () {
        var JavaString = Java.use("java.lang.String");
        // hook 指定方法的所有重载
        var ClassName = Java.use("com.baidu.cesium.f");
        var overloadsLength = ClassName.a.overloads.length;
        for (var i = 0; i < overloadsLength; i++) {
            ClassName.a.overloads[i].implementation = function () {
                // 遍历打印 arguments 
                for (var a = 0; a < arguments.length; a++) {
                    console.log(a + " : " + arguments[a]);
                }
                // 调用原方法
                return this.a.apply(this, arguments);
            }
        }
    });
}



function hookSearch_thisc() {
    Java.perform(function () {
        var JavaString = Java.use("java.lang.String");
        var utils = Java.use("com.baidu.cesium.h");
        utils.b.overload('java.lang.String').implementation = function (str) {
            var retval = null;
            console.log("str : ", str)
            printStacks();
            retval = this.b(str);
            return retval;
        }
    })
}

function hookSearch_thisF() {
    Java.perform(function () {
        var JavaString = Java.use("java.lang.String");
        var utils = Java.use("com.baidu.cesium.f");
        utils.b.overload('java.lang.String').implementation = function (str) {
            var retval = null;
            console.log("str : ", str)
            printStacks();
            retval = this.b(str);
            return retval;
        }
    })
}
function hookFile() {
    Java.perform(function () {
        var JavaString = Java.use("java.lang.String");
        var utils = Java.use("com.baidu.cesium.m.a");
        utils.a.overload('java.io.File', 'java.lang.String', 'java.lang.String', 'boolean').implementation = function (file, str1, str2, z) {
            var retval = null;
            console.log("file : ", file)
            console.log("str1 : ", str1)
            console.log("str2 : ", str2)
            console.log("z : ", z)
            retval = this.a(file, str1, str2, z);
            console.log("retval : ", retval)
            return retval;
        }
    })
}








function hook_nativeAddlibbase64encoder() {
    var soAddr = Module.findBaseAddress("libbase64encoder_v1_4.so");
    console.log(soAddr);
    var funcAddr = soAddr.add(0x11458 + 1); //函数地址计算 thumb+1 ARM不加
    console.log(funcAddr);

    if (funcAddr != null) {
        var args0, args1;
        Interceptor.attach(funcAddr, {
            onEnter: function (args) {
                args0 = args[0];
                args1 = args[1];
                console.log("args0 : ", args0.toInt32() + "\t\n");
                console.log("args1 : ", args1.toInt32() + "\t\n");
            },
            onLeave: function (retval) {
                console.log("result args0 : ", args0.toInt32() + "\t\n");
                console.log("result args1 : ", args1.toInt32() + "\t\n");
                console.log("retval : ", retval.toInt32() + "\t\n");
            }
        });
    }
}



function inline_hook() {
    var libnative_lib_addr = Module.findBaseAddress("libbase64encoder_v1_4.so");
    if (libnative_lib_addr) {
        console.log("libbase64encoder_v1_4.so:", libnative_lib_addr);
        var addr_6A5C = libnative_lib_addr.add(0x6AA8);
        console.log("addr_6A5C:", addr_6A5C);

        Java.perform(function () {
            Interceptor.attach(addr_6A5C, {
                onEnter: function (args) {
                    console.log("addr_101F4 OnEnter :", hexdump(this.context.R0));
                },
                onLeave: function (retval) {
                    console.log("retval is :", retval)
                }
            }
            )
        })
    }
}




function main() {
    // hook_generateImageKey();
    // hook_getBaiduCuid();
    // hook_HzaUid();
    // hook_UtilsKt();
    // hookUid_c();
    // hook_append();
    // hook_tosubString();
    // hookUid();
    // hook_toString();


    // Cookie port
    // hook_getCookieStr()
    // hook_getCookie();
    // hook_getHttpConfigStatus();
    // hook_getUid();
    // hook_getInfoParam();

    // hookBase64Encoder();
    // hookuid_md5();
    // hookuid_mod();
    // hookuid_ga();

    // invoke_Base64Encoder();
    invoke_Base64Encoder_Plus();
    // hookuid_c();
    // hook_overloads();
    // hookSearch_thisc();

    // UID 加密前 ｜V 生成点
    // hookSearch_thisF();
    // hookFile();

    // hook_nativeAddlibbase64encoder();
    // inline_hook();
}



setImmediate(main);