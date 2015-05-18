package test;
//http://sourceforge.net/projects/tgxn317


public class WorldController {

    private boolean method324(int i, int j, int k, int choice) {
        for (int l = 0; l < 475; l++) {
           // Class47 class47 = aClass47Array476[l];
            if (choice == 1) {
                int i1 = 792 - i;
                if (i1 > 0) {
                    int j2 = 794 + (801 * i1 >> 8);
                    int k3 = 795 + (802 * i1 >> 8);
                    int l4 = 796 + (803 * i1 >> 8);
                    int i6 = 797 + (804 * i1 >> 8);
                    if (k >= j2 && k <= k3 && j >= l4 && j <= i6) {
                        return true;
                    }
                }
            } else if (choice == 2) {
                int j1 = i - 792;
                if (j1 > 0) {
                    int k2 = 794 + (801 * j1 >> 8);
                    int l3 = 795 + (802 * j1 >> 8);
                    int i5 = 796 + (803 * j1 >> 8);
                    int j6 = 797 + (804 * j1 >> 8);
                    if (k >= k2 && k <= l3 && j >= i5 && j <= j6) {
                        return true;
                    }
                }
            } else if (choice == 3) {
                int k1 = 794 - k;
                if (k1 > 0) {
                    int l2 = 792 + (799 * k1 >> 8);
                    int i4 = 793 + (800 * k1 >> 8);
                    int j5 = 796 + (803 * k1 >> 8);
                    int k6 = 797 + (804 * k1 >> 8);
                    if (i >= l2 && i <= i4 && j >= j5 && j <= k6) {
                        return true;
                    }
                }
            } else if (choice == 4) {
                int l1 = k - 794;
                if (l1 > 0) {
                    int i3 = 792 + (799 * l1 >> 8);
                    int j4 = 793 + (800 * l1 >> 8);
                    int k5 = 796 + (803 * l1 >> 8);
                    int l6 = 797 + (804 * l1 >> 8);
                    if (i >= i3 && i <= j4 && j >= k5 && j <= l6) {
                        return true;
                    }
                }
            } else if (choice == 5) {
                int i2 = j - 796;
                if (i2 > 0) {
                    int j3 = 792 + (799 * i2 >> 8);
                    int k4 = 793 + (800 * i2 >> 8);
                    int l5 = 794 + (801 * i2 >> 8);
                    int i7 = 795 + (802 * i2 >> 8);
                    if (i >= j3 && i <= k4 && k >= l5 && k <= i7) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
