package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/** A test class constructed by TDD for {@link dig.stringLiterals( @author Yossi Gil
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue404 {
  /** Correct way of trimming does not change */
  @Test public void Z$140() {
    trimmingOf("a").stays();
  }
  /** Ensure that there is a type named {@link dig} */
  @Test public void a() {
   dig.class.hashCode(); 
  }
}
