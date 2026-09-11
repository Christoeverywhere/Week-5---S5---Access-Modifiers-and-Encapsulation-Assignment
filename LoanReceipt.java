```java
public class LoanReceipt {

    private final String memberId;
    private final String[] bookIds;

    public LoanReceipt(String memberId, String[] bookIds) {
        this.memberId = memberId;

        if (bookIds == null) {
            this.bookIds = new String[0];
        } else {
            this.bookIds = new String[bookIds.length];

            for (int i = 0; i < bookIds.length; i++) {
                this.bookIds[i] = bookIds[i];
            }
        }
    }

    public String getMemberId() {
        return memberId;
    }

    public String[] getBookIds() {

        String[] copy = new String[bookIds.length];

        for (int i = 0; i < bookIds.length; i++) {
            copy[i] = bookIds[i];
        }

        return copy;
    }

    public LoanReceipt withCorrectedBookId(int index, String newId) {

        String[] correctedIds = getBookIds();

        if (index >= 0 && index < correctedIds.length) {
            correctedIds[index] = newId;
        }

        return new LoanReceipt(memberId, correctedIds);
    }
}

class ReferenceOnlyLoanReceipt extends LoanReceipt {

    private final String roomNumber;

    public ReferenceOnlyLoanReceipt(String memberId,
                                    String[] bookIds,
                                    String roomNumber) {
        super(memberId, bookIds);
        this.roomNumber = roomNumber;
    }

    public String getRoomNumber() {
        return roomNumber;
    }
}

class CirculationLedger {

    private static String branchCode;

    static {
        branchCode = "PT-BRANCH-01";
    }

    static String processNightlyCirculation(LoanReceipt[] receipts) {

        int processed = 0;
        int nullSkipped = 0;
        int referenceOnly = 0;
        int regular = 0;

        if (receipts == null) {
            return "0 processed | 0 null skipped | 0 reference-only | 0 regular";
        }

        for (int i = 0; i < receipts.length; i++) {

            LoanReceipt receipt = receipts[i];

            if (receipt == null) {
                nullSkipped++;
                continue;
            }

            processed++;

            if (receipt instanceof ReferenceOnlyLoanReceipt) {
                referenceOnly++;
            } else {
                regular++;
            }
        }

        return processed + " processed | "
                + nullSkipped + " null skipped | "
                + referenceOnly + " reference-only | "
                + regular + " regular";
    }

    public static void main(String[] args) {

        LoanReceipt r = new LoanReceipt(
            "LIB-8841",
            new String[]{"BK-100", "BK-101"}
        );

        String[] ids = r.getBookIds();
        ids[0] = "HACKED";

        System.out.println(r.getBookIds()[0]);

        LoanReceipt corrected =
            r.withCorrectedBookId(1, "BK-102");

        System.out.println(r.getBookIds()[0]);
        System.out.println(r.getBookIds()[1]);

        System.out.println(corrected.getBookIds()[0]);
        System.out.println(corrected.getBookIds()[1]);

        LoanReceipt[] receipts = {
            new ReferenceOnlyLoanReceipt(
                "LIB-001",
                new String[]{"BK-200"},
                "Reading Room 3"
            ),
            null,
            new LoanReceipt(
                "LIB-002",
                new String[]{"BK-201"}
            )
        };

        System.out.println(
            CirculationLedger.processNightlyCirculation(receipts)
        );
    }
}
```
