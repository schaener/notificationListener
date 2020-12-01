package com.example.testios.Services;

import android.content.ComponentName;
import android.content.Context;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static android.content.ContentValues.TAG;

public class RegisterAsSystemService {

    public static void registererService(MyService ns, Context ctx) {
        String className = "android.service.notification.NotificationListenerService";
        try {

            @SuppressWarnings("rawtypes")
            Class NotificationListenerService = Class.forName(className);

            //Parameters Types
            //you define the types of params you will pass to the method
            @SuppressWarnings("rawtypes")
            Class[] paramTypes= new Class[3];
            paramTypes[0]= Context.class;
            paramTypes[1]= ComponentName.class;
            paramTypes[2] = int.class;

            Method register = NotificationListenerService.getMethod("registerAsSystemService", paramTypes);

            //Parameters of the registerAsSystemService method (see official doc for more info)
            Object[] params= new Object[3];
            params[0]= ctx;
            params[1]= new ComponentName(ctx.getPackageName(), ctx.getClass().getCanonicalName());
            params[2]= -1; // All user of the device, -2 if only current user
            // finally, invoke the function on our instance
            register.invoke(ns, params);

        } catch (ClassNotFoundException e) {
            Log.e(TAG, "Class not found", e);
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            Log.e(TAG, "No such method", e);
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            Log.e(TAG, "InvocationTarget", e);
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            Log.e(TAG, "Illegal access", e);
            e.printStackTrace();
        }

    }
}
