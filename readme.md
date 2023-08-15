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
e := method() ??? errors.Errorf("%w",e) ⤴
ok := method1() &&& errors.New("ok") ✷
ok := method2() ||| errors.New("!ok")
</pre>