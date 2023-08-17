package org.q2276225819.goerrorfold;

import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingBuilder;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.util.TextRange;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GoFoldErrorBuilder implements FoldingBuilder {
    @NotNull
    @Override
    public FoldingDescriptor [] buildFoldRegions(@NotNull ASTNode root, @NotNull Document document) {

        String txt = document.getText();

        Pattern pattern = Pattern.compile(
                "(?<var1>\\S+)\\s+:?=(?<stm>[^\\r\\n]+)[\\r\\n]+"
                        +"[^\\S\\r\\n]+if (?<when>!?(?<var2>\\S+)(?: != nil| == nil)?) [{][\\r\\n]"
        );
        Matcher matcher = pattern.matcher(txt);
        List<FoldingDescriptor> lx = new ArrayList<>();
        while (matcher.find()) {
            String a = matcher.group("var1");
            String b= matcher.group("var2");
            String when = matcher.group("when");
            if(a.equals(b)){
                if(when.equals("!"+a)){
                    when=" else ";
                }else if(when.equals(a)){
                    when=" then ";
                }else if(when.equals(a+" == nil")){
                    when=" guard ";
                }else if(when.equals(a+" != nil")){
                    when=" catch ";
                }else{
                    continue;
                }
                int ss =  matcher.end("stm");
                int ee = matcher.end("when")+1;
                lx.add(new FoldingDescriptor(root,new TextRange(ss,ee), null,when));
            }
        }

        return lx.toArray(FoldingDescriptor[]::new);
    }

    @Nullable
    @Override
    public String getPlaceholderText(@NotNull ASTNode node) {
        return " ... ";
    }

    @Override
    public boolean isCollapsedByDefault(@NotNull ASTNode node) {
        return true;
    }
}
