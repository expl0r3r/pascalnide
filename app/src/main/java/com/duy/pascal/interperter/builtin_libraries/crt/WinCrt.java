package com.duy.pascal.interperter.builtin_libraries.crt;

import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContextMixin;
import com.duy.pascal.interperter.builtin_libraries.PascalLibrary;
import com.duy.pascal.interperter.builtin_libraries.annotations.PascalMethod;
import com.duy.pascal.interperter.builtin_libraries.io.IOLib;
import com.duy.pascal.interperter.builtin_libraries.io.InOutListener;
import com.duy.pascal.interperter.exceptions.runtime.WrongArgsException;
import com.duy.pascal.frontend.runnable.ConsoleHandler;

import java.util.Map;

/**
 * Created by Duy on 24-May-17.
 */

/**
 * The wincrt unit provides some auxiliary routines for use with the graph unit,
 * namely keyboard support. It has no connection with the crt unit, nor with the
 * Turbo-Pascal for Windows WinCrt unit. As such, it should not be used by end users.
 * Refer to the crt unit instead.
 * <p>
 * See in https://www.freepascal.org/docs-html/rtl/wincrt/index.html
 */
public class WinCrt implements PascalLibrary {
    public static final String NAME = "wincrt";
    private ConsoleHandler handler;
    private CrtLib crtLib;
    private IOLib ioLib;

    public WinCrt(ConsoleHandler handler) {
        this.handler = handler;
        this.crtLib = new CrtLib(handler);
        ioLib = new IOLib((InOutListener) handler);
    }


    @Override
    public boolean instantiate(Map<String, Object> pluginargs) {
        return false;
    }

    @Override
    public void shutdown() {

    }

    @Override
    public String getName() {
        return null;
    }

    @PascalMethod(description = "Pause program execution")
    public void delay(long ms) {
        crtLib.delay(ms);
    }

    @PascalMethod(description = "Check if a key was pressed.")
    public boolean keyPressed() {
        return ioLib.keyPressed();
    }

    @PascalMethod(description = "Read a key from the keyboard")
    public char readKey() {
        return ioLib.readKey();
    }


    @PascalMethod(description = "Sound system speaker")
    public void sound(Integer frequency) throws WrongArgsException {
        crtLib.sound(frequency);
    }

    @PascalMethod(description = "Stop system speaker")
    public void noSound() {
        crtLib.noSound();
    }

    @PascalMethod(description = "Stet indicated text mode")
    public void textMode(int mode) {
        crtLib.textMode(mode);
    }


    @Override
    public void declareConstants(ExpressionContextMixin parentContext) {
        crtLib.declareConstants(parentContext);
    }

    @Override
    public void declareTypes(ExpressionContextMixin parentContext) {

    }

    @Override
    public void declareVariables(ExpressionContextMixin parentContext) {

    }

    @Override
    public void declareFunctions(ExpressionContextMixin parentContext) {

    }
}
