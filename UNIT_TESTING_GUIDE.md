# Unit Testing Guide — Order Management App

## 1. What "unit testing" means here

Your Section 3 table currently records **manual testing**: you ran the app, clicked things,
and wrote down Pass/Fail. Unit testing is different — it's automated code (using **JUnit 5**)
that calls a single method directly and checks the output, with no UI and no real database.

Both are legitimate and most rubrics want to see both. This guide gets you the unit-test half.

## 2. Project setup (Maven)

If you don't already have a `pom.xml`, add these dependencies (test scope only):

```xml
<dependencies>
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter</artifactId>
        <version>5.10.2</version>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.mockito</groupId>
        <artifactId>mockito-core</artifactId>
        <version>5.11.0</version>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.testfx</groupId>
        <artifactId>testfx-junit5</artifactId>
        <version>4.0.18</version>
        <scope>test</scope>
    </dependency>
</dependencies>

<build>
  <plugins>
    <plugin>
      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-surefire-plugin</artifactId>
      <version>3.2.5</version>
    </plugin>
  </plugins>
</build>
```

Put test files under `src/test/java/Starter_App/Starter_Ap/` — same package structure as your
main code. Run everything with:

```
mvn test
```

or right-click a test class in Eclipse/IntelliJ → Run As → JUnit Test.

## 3. What to test, mapped to your actual classes

| Class | What's testable now | Effort |
|---|---|---|
| `TableViewmodel`, `Ordermodel`, `Order_usermodel` | Constructor + getters/setters | Trivial — do these first |
| `ProcessingData`, `DeliveredControllor`, `Shipping_Controllor`, `Return_Controllor`, `Return_Controllor` | `statusFilter()` return values | Trivial |
| `tablesearch` | `normalize()` — **private**, needs a small refactor (see §5) | Small refactor |
| Cancellation window (`Order_User.cancelSlectedOrder`) | 36-hour check — **inside raw SQL**, needs extraction (see §5) | Small refactor |
| `App.java`, `initialize()` methods in controllers | Needs TestFX or a real/in-memory DB — this is integration testing, not unit testing | Skip for unit tests; keep as manual/functional tests in Section 3 |

## 4. Example tests for what already works

See `TableViewmodelTest.java` and `StatusFilterTest.java` in this folder. These will pass
against your existing code with zero changes.

## 5. Two small refactors that make the rest testable

**(a) Table search — extract `normalize()`**

Currently `tablesearch.normalize()` is `private static`. Change the modifier to
`public static` (or move it to a small standalone `SearchUtils` class). No behaviour changes —
you're just exposing a pure string function so a test can call it directly:

```java
public static String normalize(String text) {
    if (text == null) return "";
    return text.trim().toLowerCase(Locale.ROOT);
}
```

Then `TablesearchTest.java` (included) can verify trimming, case-insensitivity, and null
handling without touching any JavaFX control.

**(b) Cancellation window — extract the time check out of SQL**

Right now the 36-hour rule only exists inside a SQL string, so the only way to "test" it is to
manually create old/new rows in the database. Pull the comparison into a plain Java method:

```java
// New method, e.g. in Order_User.java or a small OrderRules helper class
public static boolean isWithinCancellationWindow(LocalDateTime orderPlacedAt, LocalDateTime now) {
    return orderPlacedAt.isAfter(now.minusHours(36));
}
```

Then have `cancelSlectedOrder()` call this method before running the (now simpler) UPDATE
statement. This is exactly the kind of edge-case logic (35h59m vs 36h01m) that's tedious to
verify by clicking through the app but trivial with a unit test — see
`CancellationWindowTest.java`.

## 6. Updating your Section 3 table

For rows like 3.4 (Order_User cancellation) and 3.6 (Table search), you can now cite the actual
test method instead of just "Pass":

> Verified by `CancellationWindowTest.cancelsWhenWithin36Hours()` and
> `.rejectsWhenOver36Hours()` — both pass under `mvn test`.

That's a much stronger piece of evidence for a marker than a manual click-through note.
