package org.q2276225819.goerrorfold;

import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingBuilder;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.FoldingGroup;
import com.intellij.openapi.editor.Inlay;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.editor.EditorCustomElementRenderer;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Go3FoldErrorBuilder implements FoldingBuilder {
    @NotNull
    @Override
    public FoldingDescriptor [] buildFoldRegions(@NotNull ASTNode root, @NotNull Document document) {
        List<FoldingDescriptor> lx = new ArrayList<>();
        String txt = document.getText();


        Pattern pattern = Pattern.compile("func (?<this>[(](?<var>[^)\\s]+\\s+)?(?<type>[^)]+)[)] )");
        Matcher matcher = pattern.matcher(txt);
        while (matcher.find()) {
            FoldingGroup g = FoldingGroup.newGroup("");
            String var = matcher.group("var");
            if(!(var==null||var.charAt(0)=='_')) {
                continue;
            }

            int sss = matcher.start("this");
            int tt1 = matcher.start("type");
            int tt2 = matcher.end("type");
            int eee = matcher.end("this");

            lx.add(new FoldingDescriptor(root, new TextRange(sss, tt1), g, ""));
            lx.add(new FoldingDescriptor(root, new TextRange(tt2, eee), g, "."));
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


