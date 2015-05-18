package test;
//http://sourceforge.net/projects/tgxn317

import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.zip.*;

import java.applet.AppletContext;
import java.math.BigInteger;

public class client{

 

    //process a click on a menu item
    public boolean processMenuClick(int j, int anInt1136, int saveClickX, int saveClickY, boolean menuOpen,
    		int menuScreenArea, int menuOffsetX, int menuOffsetY, int menuWidth, int menuHeight,
    		int menuActionRow, int anInt20, int anInt21) {

    	boolean needDrawTabArea; 
    	boolean inputTaken;
    	
        if (anInt1136 == 1 && saveClickX >= 516 && saveClickY >= 160 && saveClickX <= 765 && saveClickY <= 205) {
            j = 0;
        }

        if (menuOpen) {
            if (j != 1) {
                int k = anInt20;
                int j1 = anInt21;
                if (menuScreenArea == 0) {
                    k -= 4;
                    j1 -= 4;
                }
                if (menuScreenArea == 1) {
                    k -= 553;
                    j1 -= 205;
                }
                if (menuScreenArea == 2) {
                    k -= 7;
                    j1 -= 345;
                }

                //top tabs
                if (menuScreenArea == 3) {
                    k -= 553;
                    j1 -= 205;
                }
                //bottom tabs
                if (menuScreenArea == 4) {
                    k -= 553;
                    j1 -= 205;
                }
                if (menuScreenArea == 5) {
                    k -= 516;
                    j1 -= 0;
                }

                if (k < menuOffsetX - 28 || k > menuOffsetX + menuWidth + 28 || j1 < menuOffsetY - 28 || j1 > menuOffsetY + menuHeight + 28) {
                    menuOpen = false;
                    if (menuScreenArea == 1) {
                        needDrawTabArea = true;
                    }
                    if (menuScreenArea == 2) {
                        inputTaken = true;
                    }
                }
            }

            if (j == 1) {
                int l = menuOffsetX;
                int k1 = menuOffsetY;
                int i2 = menuWidth;
                int k2 = saveClickX;
                int l2 = saveClickY;
                if (menuScreenArea == 0) {
                    k2 -= 4;
                    l2 -= 4;
                }
                if (menuScreenArea == 1) {
                    k2 -= 553;
                    l2 -= 205;
                }
                if (menuScreenArea == 2) {
                    k2 -= 7;
                    l2 -= 345;
                }
                //top tabs
                if (menuScreenArea == 3) {
                    k2 -= 553;
                    l2 -= 205;
                }
                //bottom tabs
                if (menuScreenArea == 4) {
                    k2 -= 553;
                    l2 -= 205;
                }
                if (menuScreenArea == 5) {
                    k2 -= 516;
                    l2 -= 0;
                }
                int i3 = -1;
                for (int j3 = 0; j3 < menuActionRow; j3++) {
                    int k3 = k1 + 31 + (menuActionRow - 1 - j3) * 15;
                    if (k2 > l && k2 < l + i2 && l2 > k3 - 13 && l2 < k3 + 3) {
                        i3 = j3;
                    }
                }
                if (i3 != -1) {
                   System.out.println(i3);// doAction(i3);
                }
                menuOpen = false;
                if (menuScreenArea == 1) {
                    needDrawTabArea = true;
                }
                if (menuScreenArea == 2) {
                    inputTaken = true;
                }
            }
            return true;
        } else {
            if (j == 1 && menuActionRow > 0) {
                //int i1 = menuActionID[menuActionRow - 1];
                int i1 = (int) Math.random()*1000;
                if (i1 == 632 || i1 == 78 || i1 == 867 || i1 == 431 || i1 == 53 || i1 == 74 || i1 == 454 || i1 == 539 || i1 == 493 || i1 == 847 || i1 == 447 || i1 == 1125) {
                    int l1 = (int) Math.random()*1000;// menuActionCmd2[menuActionRow - 1];
                    int j2 =(int) Math.random()*1000;// menuActionCmd3[menuActionRow - 1];
                    //Class9 class9 = Class9.aClass9Array210[j2];
                   // if (class9.aBoolean259 || class9.aBoolean235) {
                      boolean aBoolean1242 = false;
                        int anInt989 = 0;
                        int anInt1084 = j2;
                        int anInt1085 = l1;
                        int activeInterfaceType = 2;
                        int anInt1087 = saveClickX;
                        int anInt1088 = saveClickY;
//                        if (Class9.aClass9Array210[j2].anInt236 == openInterfaceID) {
//                            activeInterfaceType = 1;
//                        }
//                        if (Class9.aClass9Array210[j2].anInt236 == backDialogID) {
//                            activeInterfaceType = 3;
//                        }
                        return true;
                    //}
                }
            }
//            if (j == 1 && (anInt1253 == 1 || menuHasAddFriend(menuActionRow - 1)) && menuActionRow > 2) {
//                j = 2;
//            }
//            if (j == 1 && menuActionRow > 0) {
//                doAction(menuActionRow - 1);
//            }
//            if (j == 2 && menuActionRow > 0) {
//                determineMenuSize();
//            }
            return false;
        }
    }



    public final void calcCameraPos(int anInt1098, int anInt1099, int anInt1100, int k, int xCameraPos, int anInt1101,
    		int anInt1102, int zCameraPos, int yCameraPos, int anInt995, int anInt996, int anInt997, int anInt998, int anInt999,
    		int yCameraCurve, int xCameraCurve) {
    	
    	
        int i = anInt1098 * 128 + 64;
        int j = anInt1099 * 128 + 64;
//        int k = method42(plane, j, true, i) - anInt1100;
        if (xCameraPos < i) {
            xCameraPos += anInt1101 + ((i - xCameraPos) * anInt1102) / 1000;
            if (xCameraPos > i) {
                xCameraPos = i;
            }
        }
        if (xCameraPos > i) {
            xCameraPos -= anInt1101 + ((xCameraPos - i) * anInt1102) / 1000;
            if (xCameraPos < i) {
                xCameraPos = i;
            }
        }
        if (zCameraPos < k) {
            zCameraPos += anInt1101 + ((k - zCameraPos) * anInt1102) / 1000;
            if (zCameraPos > k) {
                zCameraPos = k;
            }
        }
        if (zCameraPos > k) {
            zCameraPos -= anInt1101 + ((zCameraPos - k) * anInt1102) / 1000;
            if (zCameraPos < k) {
                zCameraPos = k;
            }
        }
        if (yCameraPos < j) {
            yCameraPos += anInt1101 + ((j - yCameraPos) * anInt1102) / 1000;
            if (yCameraPos > j) {
                yCameraPos = j;
            }
        }
        if (yCameraPos > j) {
            yCameraPos -= anInt1101 + ((yCameraPos - j) * anInt1102) / 1000;
            if (yCameraPos < j) {
                yCameraPos = j;
            }
        }
        i = anInt995 * 128 + 64;
        j = anInt996 * 128 + 64;
        //k = method42(plane, j, true, i) - anInt997;
        k = k - anInt997;
        int l = i - xCameraPos;
        int i1 = k - zCameraPos;
        int j1 = j - yCameraPos;
        int k1 = (int) Math.sqrt(l * l + j1 * j1);
        int l1 = (int) (Math.atan2(i1, k1) * 325.94900000000001D) & 0x7ff;
        int i2 = (int) (Math.atan2(l, j1) * -325.94900000000001D) & 0x7ff;
        if (l1 < 128) {
            l1 = 128;
        }
        if (l1 > 383) {
            l1 = 383;
        }
        if (yCameraCurve < l1) {
            yCameraCurve += anInt998 + ((l1 - yCameraCurve) * anInt999) / 1000;
            if (yCameraCurve > l1) {
                yCameraCurve = l1;
            }
        }
        if (yCameraCurve > l1) {
            yCameraCurve -= anInt998 + ((yCameraCurve - l1) * anInt999) / 1000;
            if (yCameraCurve < l1) {
                yCameraCurve = l1;
            }
        }
        int j2 = i2 - xCameraCurve;
        if (j2 > 1024) {
            j2 -= 2048;
        }
        if (j2 < -1024) {
            j2 += 2048;
        }
        if (j2 > 0) {
            xCameraCurve += anInt998 + (j2 * anInt999) / 1000;
            xCameraCurve &= 0x7ff;
        }
        if (j2 < 0) {
            xCameraCurve -= anInt998 + (-j2 * anInt999) / 1000;
            xCameraCurve &= 0x7ff;
        }
        int k2 = i2 - xCameraCurve;
        if (k2 > 1024) {
            k2 -= 2048;
        }
        if (k2 < -1024) {
            k2 += 2048;
        }
        if (k2 < 0 && j2 > 0 || k2 > 0 && j2 < 0) {
            xCameraCurve = i2;
        }
    }

 
   
    
    public final boolean method85(int i, int j, int k, int l, int i1, int j1, int k1, int l1, int i2, int j2, boolean flag, int k2,
    		int [][] anIntArrayArray901, int[][] anIntArrayArray825,int[] anIntArray1280, int[] anIntArray1281,
    		int[][] aClass11Array1230, int plane, int anInt1288) {
        //resetWalk();
        int j3 = j2;
        int k3 = j1;
        anIntArrayArray901[j2][j1] = 99;
        anIntArrayArray825[j2][j1] = 0;
        int l3 = 0;
        int i4 = 0;
        anIntArray1280[l3] = j2;
        anIntArray1281[l3++] = j1;
        boolean flag1 = false;
        int j4 = anIntArray1280.length;
        int ai[][] = aClass11Array1230;
        while (i4 != l3) {
            j3 = anIntArray1280[i4];
            k3 = anIntArray1281[i4];
            i4 = (i4 + 1) % j4;
            if (j3 == k2 && k3 == i2) {
                flag1 = true;
                break;
            }
            if (i1 != 0) {
                if ((i1 < 5 || i1 == 10) ) {
                    flag1 = true;
                    break;
                }
                if (i1 < 10 ) {
                    flag1 = true;
                    break;
                }
            }
            if (k1 != 0 && k != 0) {
                flag1 = true;
                break;
            }
            int l4 = anIntArrayArray825[j3][k3] + 1;
            if (j3 > 0 && anIntArrayArray901[j3 - 1][k3] == 0 && (ai[j3 - 1][k3] & 0x1280108) == 0) {
                anIntArray1280[l3] = j3 - 1;
                anIntArray1281[l3] = k3;
                l3 = (l3 + 1) % j4;
                anIntArrayArray901[j3 - 1][k3] = 2;
                anIntArrayArray825[j3 - 1][k3] = l4;
            }
            if (j3 < 103 && anIntArrayArray901[j3 + 1][k3] == 0 && (ai[j3 + 1][k3] & 0x1280180) == 0) {
                anIntArray1280[l3] = j3 + 1;
                anIntArray1281[l3] = k3;
                l3 = (l3 + 1) % j4;
                anIntArrayArray901[j3 + 1][k3] = 8;
                anIntArrayArray825[j3 + 1][k3] = l4;
            }
            if (k3 > 0 && anIntArrayArray901[j3][k3 - 1] == 0 && (ai[j3][k3 - 1] & 0x1280102) == 0) {
                anIntArray1280[l3] = j3;
                anIntArray1281[l3] = k3 - 1;
                l3 = (l3 + 1) % j4;
                anIntArrayArray901[j3][k3 - 1] = 1;
                anIntArrayArray825[j3][k3 - 1] = l4;
            }
            if (k3 < 103 && anIntArrayArray901[j3][k3 + 1] == 0 && (ai[j3][k3 + 1] & 0x1280120) == 0) {
                anIntArray1280[l3] = j3;
                anIntArray1281[l3] = k3 + 1;
                l3 = (l3 + 1) % j4;
                anIntArrayArray901[j3][k3 + 1] = 4;
                anIntArrayArray825[j3][k3 + 1] = l4;
            }
            if (j3 > 0 && k3 > 0 && anIntArrayArray901[j3 - 1][k3 - 1] == 0 && (ai[j3 - 1][k3 - 1] & 0x128010e) == 0 && (ai[j3 - 1][k3] & 0x1280108) == 0 && (ai[j3][k3 - 1] & 0x1280102) == 0) {
                anIntArray1280[l3] = j3 - 1;
                anIntArray1281[l3] = k3 - 1;
                l3 = (l3 + 1) % j4;
                anIntArrayArray901[j3 - 1][k3 - 1] = 3;
                anIntArrayArray825[j3 - 1][k3 - 1] = l4;
            }
            if (j3 < 103 && k3 > 0 && anIntArrayArray901[j3 + 1][k3 - 1] == 0 && (ai[j3 + 1][k3 - 1] & 0x1280183) == 0 && (ai[j3 + 1][k3] & 0x1280180) == 0 && (ai[j3][k3 - 1] & 0x1280102) == 0) {
                anIntArray1280[l3] = j3 + 1;
                anIntArray1281[l3] = k3 - 1;
                l3 = (l3 + 1) % j4;
                anIntArrayArray901[j3 + 1][k3 - 1] = 9;
                anIntArrayArray825[j3 + 1][k3 - 1] = l4;
            }
            if (j3 > 0 && k3 < 103 && anIntArrayArray901[j3 - 1][k3 + 1] == 0 && (ai[j3 - 1][k3 + 1] & 0x1280138) == 0 && (ai[j3 - 1][k3] & 0x1280108) == 0 && (ai[j3][k3 + 1] & 0x1280120) == 0) {
                anIntArray1280[l3] = j3 - 1;
                anIntArray1281[l3] = k3 + 1;
                l3 = (l3 + 1) % j4;
                anIntArrayArray901[j3 - 1][k3 + 1] = 6;
                anIntArrayArray825[j3 - 1][k3 + 1] = l4;
            }
            if (j3 < 103 && k3 < 103 && anIntArrayArray901[j3 + 1][k3 + 1] == 0 && (ai[j3 + 1][k3 + 1] & 0x12801e0) == 0 && (ai[j3 + 1][k3] & 0x1280180) == 0 && (ai[j3][k3 + 1] & 0x1280120) == 0) {
                anIntArray1280[l3] = j3 + 1;
                anIntArray1281[l3] = k3 + 1;
                l3 = (l3 + 1) % j4;
                anIntArrayArray901[j3 + 1][k3 + 1] = 12;
                anIntArrayArray825[j3 + 1][k3 + 1] = l4;
            }
        }
        int anInt1264 = 0;
        if (!flag1) {
            if (flag) {
                int i5 = 100;
                for (int k5 = 1; k5 < 2; k5++) {
                    for (int i6 = k2 - k5; i6 <= k2 + k5; i6++) {
                        for (int l6 = i2 - k5; l6 <= i2 + k5; l6++) {
                            if (i6 >= 0 && l6 >= 0 && i6 < 104 && l6 < 104 && anIntArrayArray825[i6][l6] < i5) {
                                i5 = anIntArrayArray825[i6][l6];
                                j3 = i6;
                                k3 = l6;
                                anInt1264 = 1;
                                flag1 = true;
                            }
                        }

                    }

                    if (flag1) {
                        break;
                    }
                }

            }
            if (!flag1) {
                return false;
            }
        }
        i4 = 0;
        anIntArray1280[i4] = j3;
        anIntArray1281[i4++] = k3;
        if (l != -11308) {
            for (int j6 = 1; j6 > 0; j6++);
        }
        int l5;
        for (int j5 = l5 = anIntArrayArray901[j3][k3]; j3 != j2 || k3 != j1; j5 = anIntArrayArray901[j3][k3]) {
            if (j5 != l5) {
                l5 = j5;
                anIntArray1280[i4] = j3;
                anIntArray1281[i4++] = k3;
            }
            if ((j5 & 2) != 0) {
                j3++;
            } else if ((j5 & 8) != 0) {
                j3--;
            }
            if ((j5 & 1) != 0) {
                k3++;
            } else if ((j5 & 4) != 0) {
                k3--;
            }
        }

        if (i4 > 0) {
            int k4 = i4;
            if (k4 > 25) {
                k4 = 25;
            }
            i4--;
            int k6 = anIntArray1280[i4];
            int i7 = anIntArray1281[i4];
            anInt1288 += k4;
            if (anInt1288 >= 92) {
//                aClass30_Sub2_Sub2_1192.createFrame(36);
//                aClass30_Sub2_Sub2_1192.writeDWord(0);
                anInt1288 = 0;
            }
//            if (i == 0) {
////                aClass30_Sub2_Sub2_1192.createFrame(164);
////                aClass30_Sub2_Sub2_1192.writeWordBigEndian(k4 + k4 + 3);
//            }
//            if (i == 1) {
//                aClass30_Sub2_Sub2_1192.createFrame(248);
//                aClass30_Sub2_Sub2_1192.writeWordBigEndian(k4 + k4 + 3 + 14);
//            }
//            if (i == 2) {
//                aClass30_Sub2_Sub2_1192.createFrame(98);
//                aClass30_Sub2_Sub2_1192.writeWordBigEndian(k4 + k4 + 3);
//            }
//            aClass30_Sub2_Sub2_1192.method433(k6 + baseX);
//            destX = anIntArray1280[0];
//            destY = anIntArray1281[0];
//            for (int j7 = 1; j7 < k4; j7++) {
//                i4--;
//                aClass30_Sub2_Sub2_1192.writeWordBigEndian(anIntArray1280[i4] - k6);
//                aClass30_Sub2_Sub2_1192.writeWordBigEndian(anIntArray1281[i4] - i7);
//            }
//
//            aClass30_Sub2_Sub2_1192.method431(i7 + baseY);
//            aClass30_Sub2_Sub2_1192.method424(super.keyArray[5] != 1 ? 0 : 1);
//            return true;
        }
        return i != 1;
    }

    public final void method108(int anInt1278, int anInt1131, int x, int y, int anInt1014, int anInt1015,
    		int[] keyArray, int anInt1186, int anInt1187, int anInt1185, int anInt1184, byte [][][] byteGroundArray, 
    		int [][][] intGroundArray, int anInt1005, int anInt984) {
        try {
            int j = x + anInt1278;
            int k = y + anInt1131;
            if (anInt1014 - j < -500 || anInt1014 - j > 500 || anInt1015 - k < -500 || anInt1015 - k > 500) {
                anInt1014 = j;
                anInt1015 = k;
            }
            if (anInt1014 != j) {
                anInt1014 += (j - anInt1014) / 16;
            }
            if (anInt1015 != k) {
                anInt1015 += (k - anInt1015) / 16;
            }
            if (keyArray[1] == 1) {
                anInt1186 += (-24 - anInt1186) / 2;
            } else if (keyArray[2] == 1) {
                anInt1186 += (24 - anInt1186) / 2;
            } else {
                anInt1186 /= 2;
            }
            if (keyArray[3] == 1) {
                anInt1187 += (12 - anInt1187) / 2;
            } else if (keyArray[4] == 1) {
                anInt1187 += (-12 - anInt1187) / 2;
            } else {
                anInt1187 /= 2;
            }
            anInt1185 = anInt1185 + anInt1186 / 2 & 0x7ff;
            anInt1184 += anInt1187 / 2;
            if (anInt1184 < 128) {
                anInt1184 = 128;
            }
            if (anInt1184 > 383) {
                anInt1184 = 383;
            }
            int l = anInt1014 >> 7;
            int i1 = anInt1015 >> 7;
            int j1 = (int) Math.random()*100; //method42(plane, anInt1015, true, anInt1014);
            int k1 = 0;
            if (l > 3 && i1 > 3 && l < 100 && i1 < 100) {
                for (int l1 = l - 4; l1 <= l + 4; l1++) {
                    for (int k2 = i1 - 4; k2 <= i1 + 4; k2++) {
                        int l2 = (int) Math.random()*100;///plane;
                        if (l2 < 3 && (byteGroundArray[1][l1][k2] & 2) == 2) {
                            l2++;
                        }
                        int i3 = j1 - intGroundArray[l2][l1][k2];
                        if (i3 > k1) {
                            k1 = i3;
                        }
                    }

                }

            }
            anInt1005++;
            if (anInt1005 > 1512) {
                anInt1005 = 0;
//                aClass30_Sub2_Sub2_1192.createFrame(77);
//                aClass30_Sub2_Sub2_1192.writeWordBigEndian(0);
//                int i2 = aClass30_Sub2_Sub2_1192.currentOffset;
//                aClass30_Sub2_Sub2_1192.writeWordBigEndian((int) (Math.random() * 256D));
//                aClass30_Sub2_Sub2_1192.writeWordBigEndian(101);
//                aClass30_Sub2_Sub2_1192.writeWordBigEndian(233);
//                aClass30_Sub2_Sub2_1192.writeWord(45092);
//                if ((int) (Math.random() * 2D) == 0) {
//                    aClass30_Sub2_Sub2_1192.writeWord(35784);
//                }
//                aClass30_Sub2_Sub2_1192.writeWordBigEndian((int) (Math.random() * 256D));
//                aClass30_Sub2_Sub2_1192.writeWordBigEndian(64);
//                aClass30_Sub2_Sub2_1192.writeWordBigEndian(38);
//                aClass30_Sub2_Sub2_1192.writeWord((int) (Math.random() * 65536D));
//                aClass30_Sub2_Sub2_1192.writeWord((int) (Math.random() * 65536D));
//                aClass30_Sub2_Sub2_1192.writeBytes(aClass30_Sub2_Sub2_1192.currentOffset - i2);
            }
            int j2 = k1 * 192;
            if (j2 > 0x17f00) {
                j2 = 0x17f00;
            }
            if (j2 < 32768) {
                j2 = 32768;
            }
            if (j2 > anInt984) {
                anInt984 += (j2 - anInt984) / 24;
                return;
            }
            if (j2 < anInt984) {
                anInt984 += (j2 - anInt984) / 80;
                return;
            }
        } catch (Exception _ex) {
           // signlink.reporterror("glfc_ex " + ((Entity) (aClass30_Sub2_Sub4_Sub1_Sub2_1126)).x + "," + ((Entity) (aClass30_Sub2_Sub4_Sub1_Sub2_1126)).y + "," + anInt1014 + "," + anInt1015 + "," + anInt1069 + "," + anInt1070 + "," + baseX + "," + baseY);
            throw new RuntimeException("eek");
        }
    }

    public final void determineMenuSize(int i, int menuActionRow, int k, int saveClickX, 
    		int saveClickY) {
    	 boolean menuOpen ;
         int menuScreenArea; //position for menu (eg offsetID...)
         int menuOffsetX;
         int menuOffsetY;
         int  menuWidth;
         int menuHeight;
        //int i = boldFont.getTextWidth("Choose Option"); //getTextWidth
        for (int j = 0; j < menuActionRow; j++) { //go through each menu option
            //int k = boldFont.getTextWidth(menuActionName[j]); //getTextWidth on each mmenu item
            if (k > i) //if the menu item is more than choose option, then the menu width is made bigger
            {
                i = k;
            }
        }
        i += 8;
        int l = 15 * menuActionRow + 23; // each menu item is 15 px high + 21 for the top and bottom i guess

        if (saveClickX > 4 && saveClickY > 4 && saveClickX < 516 && saveClickY < 338) { //gameframe
            int i1 = saveClickX - 4 - i / 2; //determine horixontal pos
            if (i1 + i > 510) {
                i1 = 510 - i;
            }
            if (i1 < 1) {
                i1 = 1;
            }
            int l1 = saveClickY - 4;
            if (l1 + l > 333) {
                l1 = 333 - l;
            }
            if (l1 < 0) {
                l1 = 0;
            }
            menuOpen = true;
            menuScreenArea = 0; //position for menu (eg offsetID...)
            menuOffsetX = i1;
            menuOffsetY = l1;
            menuWidth = i;
            menuHeight = l; //height
        }

        if (saveClickX > 553 && saveClickY > 205 && saveClickX < 743 && saveClickY < 466) { //inventory
            int j1 = saveClickX - 553 - i / 2;
            if (j1 < 0) {
                j1 = 0;
            }
            if (j1 + i > 190) {
                j1 = 190 - i;
            }
            int i2 = saveClickY - 205;
            if (i2 < 0) {
                i2 = 0;
            }
            if (i2 + l > 261) {
                i2 = 261 - l;
            }
            menuOpen = true;
            menuScreenArea = 1;
            menuOffsetX = j1;
            menuOffsetY = i2;
            menuWidth = i;
            menuHeight = 15 * menuActionRow + 22;
        }

        if (saveClickX > 6 && saveClickY > 344 && saveClickX < 496 && saveClickY < 503) { //chatbox
            int k1 = saveClickX - 6 - i / 2;
            if (k1 < 0) {
                k1 = 0;
            } else if (k1 + i > 488) {
                k1 = 488 - i;
            }
            int j2 = saveClickY - 344;
            if (j2 < 0) {
                j2 = 0;
            } else if (j2 + l > 128) {
                j2 = 128 - l;
            }
            menuOpen = true;
            menuScreenArea = 2; //
            menuOffsetX = k1;
            menuOffsetY = j2;
            menuWidth = i;
            menuHeight = 15 * menuActionRow + 22;
        }
        //top tasbs
        if (saveClickX >= 524 && saveClickX <= 763 && saveClickY >= 168 && saveClickY < 205) {
            int k1 = saveClickX - 524 - i / 2;
            if (k1 < 0) {
                k1 = 0;
            } else if (k1 + i > 763) {
                k1 = 763 - i;
            }
            int j2 = saveClickY;
            if (j2 < 0) {
                j2 = 0;
            } else if (j2 + l > 168) {
                j2 = 168 - l;
            }
            menuOpen = true;
            menuScreenArea = 3;
            menuOffsetX = k1;
            menuOffsetY = j2;
            menuWidth = i;
            menuHeight = 15 * menuActionRow + 22;
        }
        //bottom tabs
        if (saveClickX >= 524 && saveClickX <= 763 && saveClickY >= 467 && saveClickY < 503) {
            int k1 = saveClickX - 524 - i / 2;
            if (k1 < 0) {
                k1 = 0;
            } else if (k1 + i > 763) {
                k1 = 763 - i;
            }
            int j2 = saveClickY;
            if (j2 < 0) {
                j2 = 0;
            } else if (j2 + l > 168) {
                j2 = 168 - l;
            }
            menuOpen = true;
            menuScreenArea = 4;
            menuOffsetX = k1;
            menuOffsetY = j2;
            menuWidth = i;
            menuHeight = 15 * menuActionRow + 22;
        }


        if (saveClickX >= 515 && saveClickY >= 0 && saveClickX <= 765 && saveClickY <= 169) {

            //width
            int k1 = saveClickX - 515 - i / 2;
            if (k1 < 2) {
                k1 = 3;
            }
            if (k1 + i > 249) {
                k1 = 249 - i;
            }


            //height
            int j2 = saveClickY;
            if (j2 < 0) {
                j2 = 0;
            }
            if (j2 + l > 168) {
                j2 = 168 - l;
            }


            menuOpen = true;
            menuScreenArea = 5;
            menuOffsetX = k1;
            menuOffsetY = j2;
            menuWidth = i;
            menuHeight = 15 * menuActionRow + 22;
        }
    }



    public final int method120(int i, boolean aBoolean1224, int yCameraCurve, int xCameraCurve,
    		int xCameraPos, int yCameraPos, int x, int y, int plane, byte[][][]byteGroundArray) {
        if (i <= 0) {
            aBoolean1224 = !aBoolean1224;
        }
        int j = 3;
        if (yCameraCurve < 310) {
            int k = xCameraPos >> 7;
            int l = yCameraPos >> 7;
            int i1 = x >>7 ; //((Entity) (aClass30_Sub2_Sub4_Sub1_Sub2_1126)).x >> 7;
            int j1 = y >> 7; //((Entity) (aClass30_Sub2_Sub4_Sub1_Sub2_1126)).y >> 7;
            if ((byteGroundArray[plane][k][l] & 4) != 0) {
                j = plane;
            }
            int k1;
            if (i1 > k) {
                k1 = i1 - k;
            } else {
                k1 = k - i1;
            }
            int l1;
            if (j1 > l) {
                l1 = j1 - l;
            } else {
                l1 = l - j1;
            }
            if (k1 > l1) {
                int i2 = (l1 * 0x10000) / k1;
                int k2 = 32768;
                while (k != i1) {
                    if (k < i1) {
                        k++;
                    } else if (k > i1) {
                        k--;
                    }
                    if ((byteGroundArray[plane][k][l] & 4) != 0) {
                        j = plane;
                    }
                    k2 += i2;
                    if (k2 >= 0x10000) {
                        k2 -= 0x10000;
                        if (l < j1) {
                            l++;
                        } else if (l > j1) {
                            l--;
                        }
                        if ((byteGroundArray[plane][k][l] & 4) != 0) {
                            j = plane;
                        }
                    }
                }
            } else {
                int j2 = (k1 * 0x10000) / l1;
                int l2 = 32768;
                while (l != j1) {
                    if (l < j1) {
                        l++;
                    } else if (l > j1) {
                        l--;
                    }
                    if ((byteGroundArray[plane][k][l] & 4) != 0) {
                        j = plane;
                    }
                    l2 += j2;
                    if (l2 >= 0x10000) {
                        l2 -= 0x10000;
                        if (k < i1) {
                            k++;
                        } else if (k > i1) {
                            k--;
                        }
                        if ((byteGroundArray[plane][k][l] & 4) != 0) {
                            j = plane;
                        }
                    }
                }
            }
        }
        //if ((byteGroundArray[plane][((Entity) (aClass30_Sub2_Sub4_Sub1_Sub2_1126)).x >> 7][((Entity) (aClass30_Sub2_Sub4_Sub1_Sub2_1126)).y >> 7] & 4) != 0) {
        if ((((x >> 7) + (y >> 7)) & 4) != 0) {
            j = plane;
        }
        return j;
    }

 


  

    private final void method142(int i, int j, int k, int l, int i1, int j1, int k1, int l1,
    		boolean lowMem, int plane, byte [][][]byteGroundArray) {

        if (i1 >= 1 && i >= 1 && i1 <= 102 && i <= 102) {
            if (lowMem && j != plane) {
                return;
            }
            int i2 = 0;
            byte byte0 = -1;
            boolean flag = false;
            boolean flag1 = false;
            if (j1 == 0) {
                i2 = (int) Math.random()*1000; //aClass25_946.method300(j, i1, i);
            }
            if (j1 == 1) {
                i2 = (int) Math.random()*1000; //aClass25_946.method301(j, i1, 0, i);
            }
            if (j1 == 2) {
                i2 = (int) Math.random()*1000; //aClass25_946.method302(j, i1, i);
            }
            if (j1 == 3) {
                i2 = (int) Math.random()*1000; //aClass25_946.method303(j, i1, i);
            }
            if (i2 != 0) {
                int i3 = (int) Math.random()*1000;//aClass25_946.method304(j, i1, i, i2);
                int j2 = i2 >> 14 & 0x7fff;
                int k2 = i3 & 0x1f;
                int l2 = i3 >> 6;
//                if (j1 == 0) {
//                    aClass25_946.method291(i1, j, i, (byte) -119);
//                    Class46 class46 = Class46.method572(j2);
//                    if (class46.aBoolean767) {
//                        aClass11Array1230[j].method215(l2, k2, class46.aBoolean757, true, i1, i);
//                    }
//                }
//                if (j1 == 1) {
//                    aClass25_946.method292(0, i, j, i1);
//                }
//                if (j1 == 2) {
//                    aClass25_946.method293(j, -978, i1, i);
//                    Class46 class46_1 = Class46.method572(j2);
//                    if (i1 + class46_1.anInt744 > 103 || i + class46_1.anInt744 > 103 || i1 + class46_1.anInt761 > 103 || i + class46_1.anInt761 > 103) {
//                        return;
//                    }
//                    if (class46_1.aBoolean767) {
//                        aClass11Array1230[j].method216(l2, class46_1.anInt744, i1, i, (byte) 6, class46_1.anInt761, class46_1.aBoolean757);
//                    }
//                }
//                if (j1 == 3) {
//                    aClass25_946.method294((byte) 9, j, i, i1);
//                    Class46 class46_2 = Class46.method572(j2);
//                    if (class46_2.aBoolean767 && class46_2.aBoolean778) {
//                        aClass11Array1230[j].method218(360, i, i1);
//                    }
//                }
//            }
            if (k1 >= 0) {
                int j3 = j;
                if (j3 < 3 && (byteGroundArray[1][i1][i] & 2) == 2) {
                    j3++;
                }
                //Class7.method188(aClass25_946, k, i, l, j3, aClass11Array1230[j], intGroundArray, i1, k1, j, (byte) 93);
            }
        }
    }
    }
}