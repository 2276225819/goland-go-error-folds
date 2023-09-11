goland 插件. 折叠错误处理代码 <br> 

before:<br>
<pre>
e := method()
if e != nil {
    return errors.New("%w",e)
}
</pre>
after:<br>
<pre>
e := method() catch {
    return errors.New("%w",e)
}
</pre>
one line returns/panics:<br>
<pre>
e := method() catch : errors.Errorf("%w",e) ⤴
</pre>