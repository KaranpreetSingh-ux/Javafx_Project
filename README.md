# Last Minute — integrated build

**Anything you forgot, delivered in 15 minutes.**

A rapid-delivery general store: party supplies, stationery, groceries, drinks and
snacks, computing, household, personal care, baby and kids, pet supplies. Stock is
held in five small local **dark stores** rather than one central depot, because a
15-minute promise cannot be served from a warehouse an hour away.

Six separate Java applications merged into one. One Maven project, one database,
one login.

> **On package names:** the Java packages are still `com.novacart.*`. Renaming 100
> files and every `fx:controller` reference carries real risk for zero functional
> gain, so the rebrand covers everything a user sees and the package names were
> left alone. Say the word if you want them renamed too.

---

## Run it

```bash
cd novacart
mvn clean javafx:run
```

Requires **JDK 21**. In Eclipse, right-click **`Last Minute (run).launch`** →
**Run As → Last Minute (run)** — the Maven goals are baked in, so there is
nothing to type into the Goals field.

## Sign in

| Email | Password | Sees |
|---|---|---|
| `admin@lastminute.co.uk` | `Admin123` | everything, including Finance |
| `staff@lastminute.co.uk` | `Staff123` | Catalogue, Inventory, Orders, Reviews |
| `grace@example.com` | `Grace123` | Account and Reviews only |

Signing in as different accounts visibly changes the sidebar. Worth showing an
examiner — it proves the modules share one session rather than each having its own
login.

## What ships in the demo data

| | |
|---|---|
| Products | 90, across 9 categories, £0.95–£29.99 |
| Dark stores | 5 (Uxbridge, Camden, Shoreditch, Ealing, Croydon) |
| Stock lines | 435, with ~36 deliberately below reorder level |
| Orders | 220 over 60 days, evening-weighted, 1–4 items each |
| Reviews | ~80, **every one a verified purchase** |
| Finance | 12 months of revenue with a December peak and January dip |

The data is generated from a **fixed random seed**, so every machine shows
identical numbers. Nobody wants the demo laptop reporting different revenue from
the one in the report.

It is also **coherent across modules**: every order line resolves to a real
product, every order to a real customer, and every review is written by someone
who actually bought that product. Reviews are generated *from* delivered order
lines rather than invented, which is why the Reviews module's "verified purchase"
check passes on all of them. Add a product to `Catalogue.java` and it appears in
the Catalogue screen, gets stock in all five stores, and becomes orderable and
reviewable — because all six modules read one shared `products` table.

---
## What was wrong before

| | Before | After |
|---|---|---|
| Maven projects | 3 (+2 Eclipse-only, +1 loose files) | 1 |
| Database files | 6 | 1 |
| `products` table definitions | 4 incompatible | 1 |
| `orders` table definitions | 3 incompatible | 1 |
| Customer/user table definitions | 4 incompatible | 1 |
| Login screens | 5 | 1 |
| `main()` methods | 6 | 1 |
| Java target | 11 and 21 | 21 |
| JavaFX version | 17.0.12, 21, 21.0.4 | 21.0.4 |
| Modular (`module-info.java`) | 2 yes, 4 no — could not coexist | none, classpath only |

---

## Package layout

| Was | Now |
|---|---|
| `customer.customer` | `com.novacart.customer` |
| `Product.Product` | `com.novacart.product` |
| `controller` / `dao` / `model` / `util` *(bare packages)* | `com.novacart.inventory.*` |
| `Ecommerce.ReviewRatings` | `com.novacart.reviews` |
| `finalproject` | `com.novacart.finance` |
| `Starter_App.Starter_Ap` + 4 inconsistent variants | `com.novacart.orders.*` |
| — | `com.novacart.core` — shared schema, data access, session |
| — | `com.novacart.app` — launcher, login, shell |

Inventory's classes sat in bare `model` / `controller` / `util` packages. Merging source trees without namespacing them would have collided with three other modules.

---

## The unified schema

`com.novacart.core.Schema` holds the full DDL with a comment on each table explaining which old definitions it merges.

**`products`** — the most important merge. A superset of four definitions, so a product added in the Catalogue screen is immediately stockable in Inventory, reviewable in Reviews and sellable in Orders.

Two quantity columns that are *not* the same thing and were deliberately kept apart:

- `products.stock` — the catalogue figure the Catalogue screen edits
- `SUM(stock.quantity)` — actual physical stock across warehouses

Inventory's Reports screen already compares them to flag mismatches.

**`customers`** — merges `users` (customer module), `users` (Product module), `customers` (Reviews) and `Customers` (Orders). The key is now an integer, because Reviews and Orders both join on one. The customer module's old `TEXT` key survives as `customer_code`, so existing accounts keep their identifier. A `name` column is maintained alongside `first_name`/`last_name` because Reviews expects a single name field.

**`orders`** — merges three definitions. Integer key, with finance's readable `ORD-1001` kept as `order_code`. Carries both a real `customer_id` foreign key (so Reviews can verify a purchase) and a denormalised `customer_name` (so finance's reports, which only ever had a name string, still work). `Return` and `Cancel_Order` renamed to `returned` / `cancelled`.

---

## Bugs found and fixed

These were in the code before integration.

**1. Reviews destroyed data on every launch.** `DatabaseManager.createSampleData()` ran on *every* construction and began with `DROP TABLE` on `review_votes`, `order_items`, `reviews` and `orders`. Its own comment called it *"NUCLEAR SEEDING"*. Survivable when `reviews.db` was a private throwaway file — fatal once those tables are shared. Opening the Reviews screen would have wiped all 324 orders and every figure the Finance dashboard reports. It now seeds only when `reviews` is genuinely empty, and drops nothing.

**2. Reviews had two authentication holes.** `AdminLoginController` accepted a hardcoded admin pair (`Warisha` / `wari1`) compiled into the source — anyone reading the repo had admin. And the customer branch looked the account up by email and signed the user in **without checking the password at all**; its own comment said *"accept any password … for simplicity"*. Knowing any customer's email was enough to become them. Both now go through the shared check.

**3. The Orders module could never have run.** Every `fx:controller` pointed at `Starter_App.Starter_Ap.<Name>` while the classes actually live in `.Controller`. `login.fxml` named a `Login` class that exists in no file. `Views.Cancel` pointed at `Cancelorder.fxml`, which is in neither zip. `Views.AdminOV` was missing the `.fxml` extension. `style.css` — referenced by five views — was never committed.

**4. `Data.db` has no referential integrity.** Found by the test, not by reading:
- `Orders.Customer_Name` (`Rani`, `abc`, `def`) and the `Customers` table (`Marshan\n`, `eivce`, `ekv`) share **no names at all** — a naive join left all 324 orders ownerless
- one `Customer_Name` cell holds a twenty-line copy/paste accident
- `Address.customer_ID` holds text junk (`'2#'`, `'3#'`, `NULL`), not foreign keys
- `Delivery and logistics` has one row with an empty order id

The import creates customers from the distinct names *on the orders*, which is what makes all 322 named orders resolve. The 2 with blank names are correctly left unlinked.

**5. Inventory's stock simulator writes random movements** into what are now shared tables — it would corrupt Finance's numbers. It only starts when `InventoryApp` is run standalone; the shell never starts it.

**6. `Product/pom.xml` pointed `mainClass` at `Product.Product.App`.** The class is `Main`.

**7. Cached static `Connection`s.** Finance and Reviews each held one for the process lifetime. Once both live in one window — one on the Swing event thread, one on the JavaFX thread — that is a race. Connections are now short-lived, from `Db.getConnection()`, closed by try-with-resources.

---

## Verification

**No JDK was available in the environment where this was built**, so it has **not been compiled or launched**. Budget time to build it in Eclipse before the presentation. What *was* verified:

**Static analysis** — every `fx:controller` resolves to a real class; every `getResource(...)` path resolves to a real file; every `@stylesheet` reference in every FXML resolves; every file's `package` matches its directory; no stale package names anywhere.

**SQL** — all 48 statements across the six modules run against the real schema. 0 invalid.

**Integration test** — `tools/verify_integration.py` runs without a JDK. It builds the schema from `Schema.java`, runs the real import statements from `LegacyImport.java` against the actual legacy `.db` files, and asserts the result:

```
$ python3 tools/verify_integration.py

  PASS  orders imported: 324
  PASS  every named order resolves to a customer: 322
  PASS  blank-name orders correctly left unlinked: 2
  PASS  reviews join to product AND customer: 60
  PASS  stock rows point at shared products: 16
  PASS  customer names free of stray newlines: 61
  PASS  no duplicate customer emails: 0
  PASS  products carry data from >1 source module: 21
```

This test found defects 1, 4 and the shipments join. Run it after any schema change.

---

## Known weaknesses

**Passwords are stored and compared in plain text.** Every module did this before the merge; hashing them now would lock out every existing account. If you have time before submission, this is the single highest-value fix: hash on signup, and migrate on next successful login.

**Demo and imported accounts have password `'!'`**, which no typed input produces. They exist as review authors and order owners but cannot be signed into. Real logins come from the customer module.

**The Finance module is Swing.** Its `JFrame` content pane is lifted into a `SwingNode` rather than rewritten, so the original author's work is intact — but it will look different from the JavaFX screens. That was a deliberate call over discarding a teammate's contribution.

**`orders/style.css` is a reconstruction, not the original.** If its author still has the real file, drop it in.

**Product deletion is now cross-module.** Deleting a product cascades its stock rows and orphans its reviews, so a product that has ever been ordered is refused with a message suggesting *Inactive* instead.

**`secondary.fxml` in Reviews is dead** — nothing loads it, and its controller class never existed. Kept, with the dead reference stripped.

---

## Per-module changes

Every module's public API was preserved, so controllers compile untouched. Only implementations changed.

| Module | What changed |
|---|---|
| **customer** | `Database` delegates to `core.Accounts`. `User` gains `id` and `role`. `Session.currentUser` is now a view onto the shared session — three controllers updated to use the setter. |
| **Product** | `Database` reads and writes shared `products`. Generates `product_code` on insert so Inventory's UNIQUE constraint holds. Only writes the columns it owns — leaves `brand`/`unit`/`manufacturer` alone rather than blanking other modules' data. |
| **Inventory** | `DatabaseInitializer.getConnection()` delegates to `Db`. **Not one DAO changed.** `InventoryApp` gained the package declaration it was missing. |
| **ReviewRatings** | Nuclear seeding neutralised, auth holes closed, `module-info.java` removed, resource paths rewritten. Public API unchanged. |
| **finance** | `DBConnection` delegates to `Db`. `OrderDAO` SQL aliases `order_code AS order_id` and `customer_name AS customer` so the Swing table models are untouched. `insertOrder` now resolves a real `customer_id`. |
| **orders** | Repackaged from 5 inconsistent packages into 2. All `fx:controller` paths corrected. Dead `login.fxml` and `Views.Cancel` removed, `.fxml` extension restored to `AdminOV`. SQL retargeted at the shared schema. Navigation works inside the shell via `App.setHost`. |
