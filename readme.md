goland 插件. 折叠错误处理代码 <br> 

before:<br>
<pre>
e := method()
if e != nil {
    return e
}
</pre>
after:<br>
<pre>
e := method() ? return e
</pre>