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

        Pattern pattern = Pattern.compile( "(\\S+)\\s+:?=([^\\r\\n]+)[\\r\\n]+\\s*if (\\S+) != nil [{][\r\n]\\s+([^\r\n]*[\r\n]+)\\s+[}](?=[\r\n])");
        Matcher matcher = pattern.matcher(document.getText());

        List<FoldingDescriptor> lx = new ArrayList<>();
        while (matcher.find()) {
            String a=matcher.group(1);
            String b=matcher.group(3);
            if(a.equals(b)){
                lx.add(new FoldingDescriptor(root,
                        new TextRange(   matcher.end(2),matcher.end(0)),
                        null, " ! "+ matcher.group(4) + " ")
                );
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
