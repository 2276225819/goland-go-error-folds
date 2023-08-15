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

        Pattern pattern = Pattern.compile( "(\\S+)\\s+:?=([^\\r\\n]{0,120})[\\r\\n]+"
                +"\\s+(?:if (?<t1>\\S+) != nil |if (?<t2>!?\\S+) )[{][\\r\\n]"
                +"\\s+(?<then>[^\\r\\n]*|[^\\r\\n]+[\\r\\n]+\\s+(?:continue|break))[\\r\\n]+"
                +"\\s+[}](?=[\\r\\n])");
        Matcher matcher = pattern.matcher(txt);
        List<FoldingDescriptor> lx = new ArrayList<>();
        while (matcher.find()) {
            String a = matcher.group(1);
            String t1= matcher.group("t1");
            String t2= matcher.group("t2");
            if(a.equals(t1) || a.equals(t2)|| (t2!=null && a.equals(t2.substring(1))) ){
                String r = matcher.group("then");
                String t = a.equals(t1)?" ??? ":(t2.charAt(0)=='!'?" ||| ":" &&& ");
                if(r.startsWith("return")){
                    r = r.substring(6).trim() + " ⤴";
                }else if(r.startsWith("panic")){
                    r = r.substring(6,r.length()-1-1).trim() +  " ✷";
                }else if(r.endsWith("continue")){
                    r = r.substring(0,r.length()-8).trim() +  " ↰";
                }else if(r.endsWith("break")){
                    r = r.substring(0,r.length()-5).trim() +  " ↲";
                }else{
                    r = r.trim();
                }
                int ss =  matcher.end(2);
                int ee = matcher.end(0);
                lx.add(new FoldingDescriptor(root,new TextRange(ss,ee), null, t + r));
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
