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
        List<FoldingDescriptor> lx = new ArrayList<>();
        String txt = document.getText();

        Pattern pattern = Pattern.compile("[\\r\\n]\\s+(var |\\S+, )*"
                + "(?<var1>\\S+)\\s+:?=(?<stm>[^\\r\\n]+)[\\r\\n]+"
                + "[^\\S\\r\\n]+"
                + "if (?<when>[^\\r\\n]+) [{]"
        );
        Matcher matcher = pattern.matcher(txt);
        while (matcher.find()) {
            String var1 = matcher.group("var1");
            String when = matcher.group("when");
            int ss = matcher.end("stm");
            int ee = matcher.end("when") + 1;
            if (when.equals(var1 + " == nil")) {
                when = " empty ";
            } else if (when.equals(var1 + " != nil")) {
                when = " catch ";
            } else if (when.equals("!" + var1)) {
                when = " else ";
            } else if (when.equals(var1)) {
                when = " then ";
            } else if (when.endsWith("(" + var1 + ")")){
                when = " ?";
                ee = matcher.start("when") - 1;
            } else {
                continue;
            }
            lx.add(new FoldingDescriptor(root, new TextRange(ss, ee), null, when));
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
