package org.spartan.refactoring.wring;

import static org.spartan.refactoring.utils.Funcs.duplicate;
import static org.spartan.refactoring.utils.Funcs.elze;
import static org.spartan.refactoring.utils.Funcs.then;

import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.Statement;
import org.spartan.refactoring.utils.Is;
import org.spartan.refactoring.utils.Subject;

/**
 * /** A {@link Wring} to convert <code>if (x)
 *   return b;
 * else {
 * }</code> into <code>if (x)
 *   return b;</code>
 *
 * @author Yossi Gil
 * @since 2015-08-01
 */
public final class IfDegenerateElse extends Wring.ReplaceCurrentNode<IfStatement> {
  @Override Statement replacement(final IfStatement s) {
    final IfStatement $ = duplicate(s);
    $.setElseStatement(null);
    return !Is.blockRequiredInReplacement(s, $) ? $ : Subject.statement($).toBlock();
  }
  @Override boolean scopeIncludes(final IfStatement s) {
    return s != null && then(s) != null && degenerateElse(s);
  }
  @Override String description(@SuppressWarnings("unused") final IfStatement _) {
    return "Remove vacuous 'else' branch";
  }
  static boolean degenerateElse(final IfStatement s) {
    return elze(s) != null && Wrings.emptyElse(s);
  }
}