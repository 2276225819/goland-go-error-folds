package org.q2276225819.goerrorfold;

import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingBuilder;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.FoldingGroup;
import com.intellij.openapi.util.TextRange;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Go2FoldErrorBuilder implements FoldingBuilder {
    @NotNull
    @Override
    public FoldingDescriptor [] buildFoldRegions(@NotNull ASTNode root, @NotNull Document document) {
        List<FoldingDescriptor> lx = new ArrayList<>();
        String txt = document.getText();
        {
            Pattern pattern = Pattern.compile("[\\r\\n]\\s+"
                    + "if [^\\r\\n]+ [{]"
                    + "(?<s1>\\s+)\\S[^\\r\\n]*((?<s3>\\s+)(continue|break))?"
                    + "(?<s2>\\s+)[}](?! else )"
            );
            Matcher matcher = pattern.matcher(txt);
            while (matcher.find()) {
                String a = matcher.group(0);
                if (a.length() > 80) {
                    continue;
                }
                FoldingGroup g = FoldingGroup.newGroup("");
                for (int i = 1; i <= 3; i++) {
                    int sss = matcher.start("s" + i);
                    int eee = matcher.end("s" + i);
                    if (sss >= 0 && eee >= 0) {
                        lx.add(new FoldingDescriptor(root, new TextRange(sss, eee), g, " "));
                    }
                }
            }
        }
        {
            Pattern pattern = Pattern.compile("[\\r\\n]\\s+"
                    + "(?:case|default)[^\\r\\n]*:"
                    + "(?<s1>\\s+)\\S[^\\r\\n]*((?<s2>\\s+)(continue|break))?"
                    + "(?=\\s+(?:case|default|[}]))"
            );
            Matcher matcher = pattern.matcher(txt);
            while (matcher.find()) {
                String a = matcher.group(0);
                if (a.length() > 80) {
                    continue;
                }
                FoldingGroup g = FoldingGroup.newGroup("");
                for (int i = 1; i <= 2; i++) {
                    int sss = matcher.start("s" + i);
                    int eee = matcher.end("s" + i);
                    if (sss >= 0 && eee >= 0) {
                        lx.add(new FoldingDescriptor(root, new TextRange(sss, eee), g, " "));
                    }
                }
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
