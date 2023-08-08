package org.example.gofolderror;

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

public class GoBlockFoldingBuilder implements FoldingBuilder {
    @NotNull
    @Override
    public FoldingDescriptor [] buildFoldRegions(@NotNull ASTNode root, @NotNull Document document) {

        String txt = document.getText();

        Pattern pattern = Pattern.compile( "(\\S+)\\s+:?=([^\\r\\n]+)[\\r\\n]+\\s*(?:if (?<t1>\\S+) != nil |if !(?<t2>\\S+) )[{][\r\n]\\s+(?<v>[^\r\n]*[\r\n]+)\\s+[}](?=[\r\n])");
        Matcher matcher = pattern.matcher(txt);
        List<FoldingDescriptor> lx = new ArrayList<>();
        while (matcher.find()) {
            String a = matcher.group(1);
            String t1= matcher.group("t1");
            String t2= matcher.group("t2");
            String r = matcher.group("v");
            int ss =  matcher.end(2);
            int ee = matcher.end(0);
            if(a.equals(t1) || a.equals(t2)){
                lx.add(new FoldingDescriptor(root,new TextRange( ss,ee), null, " ! "+ r + " "));
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
