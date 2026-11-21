package com.onyx.jiohook;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class MainHook implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
        if (!lpparam.packageName.equals("com.jio.jpss")) return;
        XposedBridge.log("Onyx Hook: Loaded Jio!");

        Class<?> reactUtilsClass;
        try {
            reactUtilsClass = XposedHelpers.findClass("com.jio.jpss.AndroidReactUtils", lpparam.classLoader);
        } catch (Throwable t) { return; }

        try {
            XposedHelpers.findAndHookMethod(reactUtilsClass, "startSimAuth",
                    String.class, String.class, "com.facebook.react.bridge.Promise",
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            Object promise = param.args[2];
                            if (promise != null) XposedHelpers.callMethod(promise, "resolve", "{\"status\":\"true\"}");
                            param.setResult(null);
                        }
                    });
        } catch (Throwable t) {}

        try {
            XposedHelpers.findAndHookMethod(reactUtilsClass, "getSimAuthStatus",
                    String.class, "com.facebook.react.bridge.Promise",
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            Object promise = param.args[1];
                            if (promise != null) XposedHelpers.callMethod(promise, "resolve", "{\"status\":\"true\"}");
                            param.setResult(null);
                        }
                    });
        } catch (Throwable t) {}
    }
}
