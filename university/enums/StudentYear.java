package university.enums;
public enum StudentYear {
   FIRST(1),
   SECOND(2),
   THIRD(3),
   FOURTH(4);

   private final int value;

   private StudentYear(int var3) {
      this.value = var3;
   }

   public int getValue() {
      return this.value;
   }
}
