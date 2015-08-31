package org.spartan.refactoring.wring;

import static org.spartan.refactoring.utils.Funcs.*;
import org.spartan.refactoring.utils.Extract;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Assignment;
import static org.eclipse.jdt.core.dom.Assignment.Operator.ASSIGN;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;
import org.spartan.refactoring.utils.Occurrences;

/**
 * A {@link Wring} to convert <code>int a;
 * a = 3;</code> into <code>int a = 3;</code>
 *
 * @author Yossi Gil
 * @since 2015-08-07
 */
public final class DeclarationInitialiazerAssignment extends Wring.ReplaceToNextStatement<VariableDeclarationFragment> {
  @Override ASTRewrite go(final ASTRewrite r, final VariableDeclarationFragment f, final Statement nextStatement, final TextEditGroup g) {
    final Expression firstInitializer = f.getInitializer();
    if (firstInitializer == null)
      return null;
    final Assignment a = Extract.assignment(nextStatement);
    if (a == null || !same(f.getName(), left(a)) || a.getOperator() != ASSIGN)
      return null;
    final SimpleName name = f.getName();
    final Expression secondInitializer = right(a);
    final List<Expression> uses = Occurrences.USES_SEMANTIC.of(name).in(secondInitializer);
    if (uses.size() == 1 && Occurrences.ASSIGNMENTS.of(name).in(secondInitializer).isEmpty()) {
      r.remove(Extract.statement(a), g);
      final ASTNode betterInitializer = duplicate(secondInitializer);
      r.replace(firstInitializer, betterInitializer, g);
      r.replace(Occurrences.USES_SEMANTIC.of(name).in(betterInitializer).get(0), firstInitializer, g);
      return r;
    }
    return null;
  }
  private static VariableDeclarationFragment makeVariableDeclarationFragement(final VariableDeclarationFragment f, final Expression e) {
    final VariableDeclarationFragment $ = duplicate(f);
    $.setInitializer(duplicate(e));
    return $;
  }
  @Override String description(final VariableDeclarationFragment n) {
    return "Consolidate declaration of " + n.getName() + " with its subsequent initialization";
  }
}