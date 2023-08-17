goland 插件. 折叠错误处理代码 <br> 

before:<br>
<pre>
e := method()
if e != nil {
    return errors.New("%w",e)
}
ok := method1()
if ok {
    panic(errors.New("ok"))
}
ok := method2()
if !ok {
    errors.New("!ok")
}
</pre>
after:<br>
<pre>
e := method() catch {
    return errors.New("%w",e)
}
ok := method1() then {
    panic(errors.New("ok"))
}
ok := method2() else {
    errors.New("!ok")
}
</pre>
one line returns/panics:<br>
<pre>
e := method() catch : errors.Errorf("%w",e) ⤴
ok := method1() then : errors.New("ok") ✷
ok := method2() else {
    errors.New("!ok")
}
</pre>