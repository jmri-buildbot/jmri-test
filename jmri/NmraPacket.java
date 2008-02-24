
// NmraPacket.java

package jmri;

/**
 * Utilities for coding/decoding NMRA S&RP DCC packets.
 *<P>
 * Packets are (now) represented by an array of bytes. Preamble/postamble
 * not included. Note that this is a data representation, _not_ a representation
 * of the waveform!  But this is a class, which might eventually also
 * form a representation object.
 *<P>
 * This is meant to be a general Java NMRA implementation, so does NOT use
 * JMRI utilities. In particular, it returns null instead of throwing JmriException
 * for invalid requests. Callers need to check upstream.
 *<P>
 * The function is provided by static member functions; objects of this
 * class should not be created.
 *<P>
 * Note that these functions are structured by packet type, not by what want to do.  E.g.
 * there are functions to create specific packet formats instead of a general "loco speed
 * packet" routine which figures out which type of packet to use.  Those decisions
 * are to be made somewhere else.
 * <P>
 * Range and value checking is intended to be aggressive; if we can check, we
 * should.  Problems are reported as warnings.
 *<P>
 * The basic function is to build a packet with proper addressing, etc:
 * <UL>
 * <li>threeBytePacket
 * <li>threeBytePacket
 * </ul>
 * On top of those are built various special-purpose packet formats.
 * <hr>
 * This file is part of JMRI.
 * <P>
 * JMRI is free software; you can redistribute it and/or modify it under
 * the terms of version 2 of the GNU General Public License as published
 * by the Free Software Foundation. See the "COPYING" file for a copy
 * of this license.
 * <P>
 * JMRI is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * for more details.
 * <P>
 *
 * @author      Bob Jacobsen Copyright (C) 2001, 2003
 * @version     $Revision: 1.25 $
 */
public class NmraPacket {

    /**
     * Create a packet containing a two-byte instruction.
     */
    public static byte[] twoBytePacket(int address, boolean longAddr,
                            byte arg1, byte arg2) {
        if (!addressCheck(address, longAddr)) {
            return null;  // failed!
        }

        // end sanity check, format output
        byte[] retVal;
        if (longAddr) {
            // long address form
            retVal = new byte[5];
            retVal[0] = (byte) (192+((address/256)&0x3F));
            retVal[1] = (byte) (address&0xFF);
            retVal[2] = (byte) arg1;
            retVal[3] = (byte) arg2;
            retVal[4] = (byte) (retVal[0]^retVal[1]^retVal[2]^retVal[3]);
        } else {
            // short address form
            retVal = new byte[4];
            retVal[0] = (byte) (address&0xFF);
            retVal[1] = (byte) arg1;
            retVal[2] = (byte) arg2;
            retVal[3] = (byte) (retVal[0]^retVal[1]^retVal[2]);
        }
        return retVal;
    }
    
    /**
     * Create a packet containing a three-byte instruction.
     */
    public static byte[] threeBytePacket(int address, boolean longAddr,
                            byte arg1, byte arg2, byte arg3) {
        if (!addressCheck(address, longAddr)) {
            return null;  // failed!
        }

        // end sanity check, format output
        byte[] retVal;
        if (longAddr) {
            // long address form
            retVal = new byte[6];
            retVal[0] = (byte) (192+((address/256)&0x3F));
            retVal[1] = (byte) (address&0xFF);
            retVal[2] = (byte) arg1;
            retVal[3] = (byte) arg2;
            retVal[4] = (byte) arg3;
            retVal[5] = (byte) (retVal[0]^retVal[1]^retVal[2]^retVal[3]^retVal[4]);
        } else {
            // short address form
            retVal = new byte[5];
            retVal[0] = (byte) (address&0xFF);
            retVal[1] = (byte) arg1;
            retVal[2] = (byte) arg2;
            retVal[3] = (byte) arg3;
            retVal[4] = (byte) (retVal[0]^retVal[1]^retVal[2]^retVal[3]);
        }
        return retVal;
    }
    
    /**
     * Create a packet containing a four-byte instruction.
     */
    public static byte[] fourBytePacket(int address, boolean longAddr,
                            byte arg1, byte arg2, byte arg3, byte arg4) {
        if (!addressCheck(address, longAddr)) {
            return null;  // failed!
        }

        // end sanity check, format output
        byte[] retVal;
        if (longAddr) {
            // long address form
            retVal = new byte[7];
            retVal[0] = (byte) (192+((address/256)&0x3F));
            retVal[1] = (byte) (address&0xFF);
            retVal[2] = (byte) arg1;
            retVal[3] = (byte) arg2;
            retVal[4] = (byte) arg3;
            retVal[5] = (byte) arg4;
            retVal[6] = (byte) (retVal[0]^retVal[1]^retVal[2]^retVal[3]^retVal[4]^retVal[5]);
        } else {
            // short address form
            retVal = new byte[6];
            retVal[0] = (byte) (address&0xFF);
            retVal[1] = (byte) arg1;
            retVal[2] = (byte) arg2;
            retVal[3] = (byte) arg3;
            retVal[4] = (byte) arg4;
            retVal[5] = (byte) (retVal[0]^retVal[1]^retVal[2]^retVal[3]^retVal[4]);
        }
        return retVal;
    }
    

    public static byte[] accDecoderPkt(int addr, int active, int outputChannel) {
        // From the NMRA RP:
        // 0 10AAAAAA 0 1AAACDDD 0 EEEEEEEE 1
        // Accessory Digital Decoders can be designed to control momentary or
        // constant-on devices, the duration of time each output is active being controlled
        // by configuration variables CVs #515 through 518. Bit 3 of the second byte "C" is
        // used to activate or deactivate the addressed device. (Note if the duration the
        // device is intended to be on is less than or equal the set duration, no deactivation
        // is necessary.) Since most devices are paired, the convention is that bit "0" of
        // the second byte is used to distinguish between which of a pair of outputs the
        // accessory decoder is activating or deactivating. Bits 1 and 2 of byte two is used
        // to indicate which of 4 pairs of outputs the packet is controlling. The significant
        // bits of the 9 bit address are bits 4-6 of the second data byte. By convention
        // these three bits are in ones complement. The use of bit 7 of the second byte
        // is reserved for future use.

        // Note that A=1 is the first (lowest) valid address field, and the
        // largest is 512!  I don't know why this is, but it gets the
        // right hardware addresses

        if (addr < 1 || addr>511) {
            log.error("invalid address "+addr);
            return null;
        }
        if (active < 0 || active>1) {
            log.error("invalid active (C) bit "+addr);
            return null;
        }
        if (outputChannel < 0 || outputChannel>7) {
            log.error("invalid output channel "+addr);
            return null;
        }

        int lowAddr = addr & 0x3F;
        int highAddr = ( (~addr) >> 6) & 0x07;

        byte[] retVal = new byte[3];

        retVal[0] = (byte) (0x80 | lowAddr);
        retVal[1] = (byte) (0x80 | (highAddr << 4 ) | ( active << 3) | outputChannel&0x07);
        retVal[2] = (byte) (retVal[0] ^ retVal[1]);

        return retVal;
    }

    /**
     * From the NMRA RP:
     * Basic Accessory Decoder Packet address for operations mode programming
     * 10AAAAAA 0 1AAACDDD 0 1110CCVV 0 VVVVVVVV 0 DDDDDDDD
     * Where DDD is used to indicate the output whose CVs are being modified and C=1.
     * If CDDD= 0000 then the CVs refer to the entire decoder. The resulting packet would be
     * {preamble} 10AAAAAA 0 1AAACDDD 0 (1110CCVV   0   VVVVVVVV   0   DDDDDDDD) 0 EEEEEEEE 1
     * Accessory Decoder Address   (Configuration Variable Access Instruction)     Error Byte
     */
     public static byte[] accDecoderPktOpsMode(int addr, int active, int outputChannel, int cvNum, int data) {

        if (addr < 1 || addr>511) {
            log.error("invalid address "+addr);
            return null;
        }
        if (active < 0 || active>1) {
            log.error("invalid active (C) bit "+addr);
            return null;
        }
        if (outputChannel < 0 || outputChannel>7) {
            log.error("invalid output channel "+addr);
            return null;
        }

        if (cvNum < 1 || cvNum>1023) {
            log.error("invalid CV number "+cvNum);
            return null;
        }

        if (data<0 || data>255) {
            log.error("invalid data "+data);
            return null;
        }

        int lowAddr = addr & 0x3F;
        int highAddr = ( (~addr) >> 6) & 0x07;

        int lowCVnum = (cvNum-1) & 0xFF;
        int highCVnum = ((cvNum-1) >> 8) & 0x03;

        byte[] retVal = new byte[6];
        retVal[0] = (byte) (0x80 | lowAddr);
        retVal[1] = (byte) (0x80 | (highAddr << 4 ) | ( active << 3) | outputChannel&0x07);
        retVal[2] = (byte) (0xEC | highCVnum);
        retVal[3] = (byte) (lowCVnum);
        retVal[4] = (byte) (0xFF & data);
        retVal[5] = (byte) (retVal[0]^retVal[1]^retVal[2]^retVal[3]^retVal[4]);

        return retVal;
    }

     /**
      * From the NMRA RP:
      * The format for Accessory Decoder Configuration Variable Access Instructions is:
      * {preamble} 0 10AAAAAA 0 0AAA11VV 0 VVVVVVVV 0 DDDDDDDD 0 EEEEEEEE 1
      * Where:
      *  A = Decoder address bits
      *  V = Desired CV address - (CV 513 = 10 00000000)
      *  D = Data for CV
      *
      *  This is the old "legacy" format, newer decoders use the "Basic Accessory Decoder Packet"
      */

     public static byte[] accDecPktOpsModeLegacy(int addr, int cvNum, int data) {

         if (addr < 1 || addr>511) {
             log.error("invalid address "+addr);
             return null;
         }

         if (cvNum < 1 || cvNum>1023) {
             log.error("invalid CV number "+cvNum);
             return null;
         }

         if (data<0 || data>255) {
             log.error("invalid data "+data);
             return null;
         }

         int lowAddr = addr & 0x3F;
         int highAddr = ( (~addr) >> 6) & 0x07;

         int lowCVnum = (cvNum-1) & 0xFF;
         int highCVnum = ((cvNum-1) >> 8) & 0x03;

         byte[] retVal = new byte[5];
         retVal[0] = (byte) (0x80 | lowAddr);
         retVal[1] = (byte) (0x0C | (highAddr << 4 ) | highCVnum);
         retVal[2] = (byte) (lowCVnum);
         retVal[3] = (byte) (0xFF & data);
         retVal[4] = (byte) (retVal[0]^retVal[1]^retVal[2]^retVal[3]);

         return retVal;
     }




    /**
     * From the RP:
     * Extended Accessory Decoder Control Packet Format
     * The Extended Accessory Decoder Control Packet is included for the purpose
     *  of transmitting aspect control to signal decoders or data bytes to more
     *  complex accessory decoders. Each signal head can display one aspect at a time.
     *
     * {preamble} 0 10AAAAAA 0 0AAA0AA1 0 000XXXXX 0 EEEEEEEE 1
     *
     * XXXXX is for a single head. A value of 00000 for XXXXX indicates the absolute
     *  stop aspect. All other aspects represented by the values for XXXXX are
     *  determined by the signaling system used and the prototype being modeled.
     *
     * @param outputAddr Address of accessory output, starting with 1 and a maximum of 2044
     * @param aspect Aspect Number starting with 0 and a maximum of 31
     */
    public static byte[] accSignalDecoderPkt(int outputAddr, int aspect) {

      if (outputAddr < 1 || outputAddr>2044) {
          log.error("invalid address " + outputAddr);
          return null;
      }

      if (aspect < 0 || aspect >31) {
          log.error("invalid aspect "+aspect);
          return null;
      }

      outputAddr -= 1; // Make the address 0 based
      int lowAddr = (outputAddr & 0x03 ) ;  // Output Pair Address
      int boardAddr = (outputAddr >> 2) + 1 ; // Board Address
      int midAddr =  boardAddr & 0x3F ;
      int highAddr = ( (~boardAddr) >> 6) & 0x07;

      byte[] retVal = new byte[4];
      retVal[0] = (byte) (0x80 | midAddr ) ;
      retVal[1] = (byte) (0x01 | (highAddr<<4) | (lowAddr << 1)) ;
      retVal[2] = (byte) (0x1F & aspect);
      retVal[3] = (byte) (retVal[0]^retVal[1]^retVal[2]);

      return retVal;
    }
    
    /**
     * Provide an accessory control packet via a simplified interface
     * @param number Address of accessory output, starting with 1
     * @param closed true if the output is to be configured to the "closed", a.k.a. the
     * "normal" or "unset" position
     */

    public static byte[] accDecoderPkt(int number, boolean closed) {
        // dBit is the "channel" info, least 7 bits, for the packet
        // The lowest channel bit represents CLOSED (1) and THROWN (0)
        int dBits = (( (number-1) & 0x03) << 1 );  // without the low CLOSED vs THROWN bit
        dBits = closed ? (dBits | 1) : dBits;

        // aBits is the "address" part of the nmra packet, which starts with 1
        // 07/01/05 R.Scheffler - Removed the mask, this will allow any 'too high' numbers
        // through to accDecoderPkt() above which will log the error if out of bounds. If we
        // mask it here, then the number will 'wrap' without any indication that it did so.
        int aBits = (number-1) >> 2;      // Divide by 4 to get the 'base'
        aBits += 1;                       // Base is +1

        // cBit is the control bit, we're always setting it active
        int cBit = 1;

        // get the packet
        return NmraPacket.accDecoderPkt(aBits, cBit, dBits);
    }

    /**
     * Provide an operation mode accessory control packet via a simplified interface
     * @param number Address of accessory, starting with 1
     * @param cvNum CV number to access
     * @param data Data to be written
     */
    public static byte[] accDecoderPktOpsMode(int number, int cvNum, int data) {
        // dBit is the "channel" info, least 7 bits, for the packet
        // The lowest channel bit represents CLOSED (1) and THROWN (0)
        int dBits = (( (number-1) & 0x03) << 1 );  // without the low CLOSED vs THROWN bit

        // aBits is the "address" part of the nmra packet, which starts with 1
        int aBits = (number-1) >> 2;      // Divide by 4 to get the 'base'
        aBits += 1;                       // Base is +1

        // cBit is the control bit, we're always setting it active
        int cBit = 1;

        // get the packet
        return NmraPacket.accDecoderPktOpsMode(aBits, cBit, dBits, cvNum, data);
    }

    /**
     * Provide a legacy operation mode accessory control packet via a simplified interface
     * @param number Address of accessory, starting with 1
     * @param cvNum CV number to access
     * @param data Data to be written
     */
    public static byte[] accDecoderPktOpsModeLegacy(int number, int cvNum, int data) {

        // aBits is the "address" part of the nmra packet, which starts with 1
        int aBits = (number-1) >> 2;      // Divide by 4 to get the 'base'
        aBits += 1;                       // Base is +1

        // get the packet
        return NmraPacket.accDecPktOpsModeLegacy(aBits, cvNum, data);
    }
    static byte[] locoSpeed14S(int address, int speedStep, boolean F0 ) {
        if (log.isDebugEnabled()) log.debug("create "+address+" "+speedStep+" "+F0);
        if (speedStep < 0 || speedStep>14) {
            log.error("invalid speedStep "+speedStep);
            return null;
        }
        if (address < 0 || address>14) {
            log.error("invalid address "+speedStep);
            return null;
        }
        log.error("locoSpeed14S not fully implemented");
        return new byte[2];
    }

    public static byte[] opsCvWriteByte(int address, boolean longAddr, int cvNum, int data ) {
        if (log.isDebugEnabled()) log.debug("opswrite "+address+" "+cvNum+" "+data);

        if (!addressCheck(address, longAddr)) {
            return null;  // failed!
        }

        if (data<0 || data>255) {
            log.error("invalid data "+data);
            return null;
        }
        if (cvNum<1 || cvNum>512) {
            log.error("invalid CV number "+cvNum);
            return null;
        }

        // end sanity checks, format output
        byte[] retVal;
        int arg1 = 0xEC + (((cvNum-1)>>8)&0x03);
        int arg2 = (cvNum-1)&0xFF;
        int arg3 = data&0xFF;

        if (longAddr) {
            // long address form
            retVal = new byte[6];
            retVal[0] = (byte) (192+((address/256)&0x3F));
            retVal[1] = (byte) (address&0xFF);
            retVal[2] = (byte) arg1;
            retVal[3] = (byte) arg2;
            retVal[4] = (byte) arg3;
            retVal[5] = (byte) (retVal[0]^retVal[1]^retVal[2]^retVal[3]^retVal[4]);
        } else {
            // short address form
            retVal = new byte[5];
            retVal[0] = (byte) (address&0xFF);
            retVal[1] = (byte) arg1;
            retVal[2] = (byte) arg2;
            retVal[3] = (byte) arg3;
            retVal[4] = (byte) (retVal[0]^retVal[1]^retVal[2]^retVal[3]);
        }
        return retVal;
    }

    public static byte[] speedStep128Packet(int address, boolean longAddr, int speed, boolean fwd ) {
        if (log.isDebugEnabled()) log.debug("128 step packet "+address+" "+speed);

        if (!addressCheck(address, longAddr)) {
            return null;  // failed!
        }

        if (speed<0 || speed>127) {
            log.error("invalid speed "+speed);
            return null;
        }

        // end sanity checks, format output
        byte[] retVal;
        int arg1 = 0x3F;
        int arg2 = (speed&0x7F) | (fwd ? 0x80 : 0);

        if (longAddr) {
            // long address form
            retVal = new byte[5];
            retVal[0] = (byte) (192+((address/256)&0x3F));
            retVal[1] = (byte) (address&0xFF);
            retVal[2] = (byte) arg1;
            retVal[3] = (byte) arg2;
            retVal[4] = (byte) (retVal[0]^retVal[1]^retVal[2]^retVal[3]);
        } else {
            // short address form
            retVal = new byte[4];
            retVal[0] = (byte) (address&0xFF);
            retVal[1] = (byte) arg1;
            retVal[2] = (byte) arg2;
            retVal[3] = (byte) (retVal[0]^retVal[1]^retVal[2]);
        }
        return retVal;
    }

    public static byte[] function0Through4Packet(int address, boolean longAddr,
                        boolean f0, boolean f1, boolean f2, boolean f3, boolean f4 ) {
        if (log.isDebugEnabled()) log.debug("f0 through f4 packet "+address);

        if (!addressCheck(address, longAddr)) {
            return null;  // failed!
        }

        // end sanity check, format output

        byte[] retVal;
        int arg1 = 0x80 |
                    ( f0 ? 0x10 : 0) |
                    ( f1 ? 0x01 : 0) |
                    ( f2 ? 0x02 : 0) |
                    ( f3 ? 0x04 : 0) |
                    ( f4 ? 0x08 : 0);

        if (longAddr) {
            // long address form
            retVal = new byte[4];
            retVal[0] = (byte) (192+((address/256)&0x3F));
            retVal[1] = (byte) (address&0xFF);
            retVal[2] = (byte) arg1;
            retVal[3] = (byte) (retVal[0]^retVal[1]^retVal[2]);
        } else {
            // short address form
            retVal = new byte[3];
            retVal[0] = (byte) (address&0xFF);
            retVal[1] = (byte) arg1;
            retVal[2] = (byte) (retVal[0]^retVal[1]);
        }
        return retVal;
    }

    public static byte[] function5Through8Packet(int address, boolean longAddr,
                        boolean f5, boolean f6, boolean f7, boolean f8 ) {
        if (log.isDebugEnabled()) log.debug("f5 through f8 packet "+address);

        if (!addressCheck(address, longAddr)) {
            return null;  // failed!
        }

        // end sanity check, format output
        byte[] retVal;
        int arg1 = 0xB0 |
                    ( f8 ? 0x08 : 0) |
                    ( f7 ? 0x04 : 0) |
                    ( f6 ? 0x02 : 0) |
                    ( f5 ? 0x01 : 0);

        if (longAddr) {
            // long address form
            retVal = new byte[4];
            retVal[0] = (byte) (192+((address/256)&0x3F));
            retVal[1] = (byte) (address&0xFF);
            retVal[2] = (byte) arg1;
            retVal[3] = (byte) (retVal[0]^retVal[1]^retVal[2]);
        } else {
            // short address form
            retVal = new byte[3];
            retVal[0] = (byte) (address&0xFF);
            retVal[1] = (byte) arg1;
            retVal[2] = (byte) (retVal[0]^retVal[1]);
        }
        return retVal;
    }

    public static byte[] function9Through12Packet(int address, boolean longAddr,
                        boolean f9, boolean f10, boolean f11, boolean f12 ) {
        if (log.isDebugEnabled()) log.debug("f9 through f12 packet "+address);

        if (!addressCheck(address, longAddr)) {
            return null;  // failed!
        }

        // end sanity check, format output
        byte[] retVal;
        int arg1 = 0xA0 |
                    ( f12 ? 0x08 : 0) |
                    ( f11 ? 0x04 : 0) |
                    ( f10 ? 0x02 : 0) |
                    ( f9  ? 0x01 : 0);

        if (longAddr) {
            // long address form
            retVal = new byte[4];
            retVal[0] = (byte) (192+((address/256)&0x3F));
            retVal[1] = (byte) (address&0xFF);
            retVal[2] = (byte) arg1;
            retVal[3] = (byte) (retVal[0]^retVal[1]^retVal[2]);
        } else {
            // short address form
            retVal = new byte[3];
            retVal[0] = (byte) (address&0xFF);
            retVal[1] = (byte) arg1;
            retVal[2] = (byte) (retVal[0]^retVal[1]);
        }
        return retVal;
    }

    public static byte[] function13Through20Packet(int address, boolean longAddr,
                        boolean f13, boolean f14, boolean f15, boolean f16,
                        boolean f17, boolean f18, boolean f19, boolean f20 ) {
        if (log.isDebugEnabled()) log.debug("f13 through f20 packet "+address);

        if (!addressCheck(address, longAddr)) {
            return null;  // failed!
        }

        // end sanity check, format output
        byte[] retVal;
        int arg1 = 0xDE;
        int arg2 =  ( f20 ? 0x80 : 0) |
                    ( f19 ? 0x40 : 0) |
                    ( f18 ? 0x20 : 0) |
                    ( f17 ? 0x10 : 0) |
                    ( f16 ? 0x08 : 0) |
                    ( f15 ? 0x04 : 0) |
                    ( f14 ? 0x02 : 0) |
                    ( f13 ? 0x01 : 0);

        if (longAddr) {
            // long address form
            retVal = new byte[5];
            retVal[0] = (byte) (192+((address/256)&0x3F));
            retVal[1] = (byte) (address&0xFF);
            retVal[2] = (byte) arg1;
            retVal[3] = (byte) arg2;
            retVal[4] = (byte) (retVal[0]^retVal[1]^retVal[2]^retVal[3]);
        } else {
            // short address form
            retVal = new byte[4];
            retVal[0] = (byte) (address&0xFF);
            retVal[1] = (byte) arg1;
            retVal[2] = (byte) arg2;
            retVal[3] = (byte) (retVal[0]^retVal[1]^retVal[2]);
        }
        return retVal;
    }

    public static byte[] function21Through28Packet(int address, boolean longAddr,
                        boolean f21, boolean f22, boolean f23, boolean f24,
                        boolean f25, boolean f26, boolean f27, boolean f28 ) {
        if (log.isDebugEnabled()) log.debug("f21 through f28 packet "+address);

        if (!addressCheck(address, longAddr)) {
            return null;  // failed!
        }

        // end sanity check, format output
        byte[] retVal;
        int arg1 = 0xDF;
        int arg2 =  ( f28 ? 0x80 : 0) |
                    ( f27 ? 0x40 : 0) |
                    ( f26 ? 0x20 : 0) |
                    ( f25 ? 0x10 : 0) |
                    ( f24 ? 0x08 : 0) |
                    ( f23 ? 0x04 : 0) |
                    ( f22 ? 0x02 : 0) |
                    ( f21 ? 0x01 : 0);

        if (longAddr) {
            // long address form
            retVal = new byte[5];
            retVal[0] = (byte) (192+((address/256)&0x3F));
            retVal[1] = (byte) (address&0xFF);
            retVal[2] = (byte) arg1;
            retVal[3] = (byte) arg2;
            retVal[4] = (byte) (retVal[0]^retVal[1]^retVal[2]^retVal[3]);
        } else {
            // short address form
            retVal = new byte[4];
            retVal[0] = (byte) (address&0xFF);
            retVal[1] = (byte) arg1;
            retVal[2] = (byte) arg2;
            retVal[3] = (byte) (retVal[0]^retVal[1]^retVal[2]);
        }
        return retVal;
    }

    /**
     * Provide an NMRA analog control instruction
     *<P>Note that the NMRA draft of Fall 2004 only defines the value
     * of "1" for the "function parameter", calling that the value for
     * "volume control".  However, DCC systems in the wild have been
     * observed to use 0x7F for the function byte for volume control.
     * @param address  DCC locomotive address
     * @param longAddr true if this is a long address, false if short address
     * @param function see note above
     * @param value  value to be sent in analog control instruction
     */
    public static byte[]  analogControl(int address, boolean longAddr,
                                        int function, int value) {

        if (!addressCheck(address, longAddr)) {
            return null;  // failed!
        }

        // end sanity check, format output
        byte[] retVal;
        int arg1 = 0x3D;  // analog instruction tag


        if (longAddr) {
            // long address form
            retVal = new byte[6];
            retVal[0] = (byte) (192+((address/256)&0x3F));
            retVal[1] = (byte) (address&0xFF);
            retVal[2] = (byte) arg1;
            retVal[3] = (byte) (function&0xFF);
            retVal[4] = (byte) (value&0xFF);
            retVal[5] = (byte) (retVal[0]^retVal[1]^retVal[2]^retVal[3]^retVal[4]);
        } else {
            // short address form
            retVal = new byte[5];
            retVal[0] = (byte) (address&0xFF);
            retVal[1] = (byte) arg1;
            retVal[2] = (byte) (function&0xFF);
            retVal[3] = (byte) (value&0xFF);
            retVal[4] = (byte) (retVal[0]^retVal[1]^retVal[2]^retVal[3]);
        }
        return retVal;
    }

    /**
     * Provide an NMRA consist control instruction
     * @param address  DCC locomotive address
     * @param longAddr true if this is a long address, false if short address
     * @param consist the consist address to set for this locomotive. Send
     * 00 as consist address if deleteing from consist.
     * @param directionNormal true if the normal direction of travel for this
     * address is the normal direction of travel for the consist.
     */
    public static byte[]  consistControl(int address, boolean longAddr,
                                        int consist, boolean directionNormal) {

        if (!addressCheck(address, longAddr)) {
            return null;  // failed!
        } else if(!addressCheck(consist,false)) {
	    return null;  // failed - Consist address is not a short address!
	}

        // end sanity check, format output
        byte[] retVal;
        int arg1 = 0x10;  // Consist Control instruction tag
	if (directionNormal)
		arg1|=0x02;   // Forward Direction
	else
		arg1|=0x03;   // Reverse Direction

        if (longAddr) {
            // long address form
            retVal = new byte[5];
            retVal[0] = (byte) (192+((address/256)&0x3F));
            retVal[1] = (byte) (address&0xFF);
            retVal[2] = (byte) arg1;
            retVal[3] = (byte) (consist&0xFF);
            retVal[4] = (byte) (retVal[0]^retVal[1]^retVal[2]^retVal[3]);
        } else {
            // short address form
            retVal = new byte[4];
            retVal[0] = (byte) (address&0xFF);
            retVal[1] = (byte) arg1;
            retVal[2] = (byte) (consist&0xFF);
            retVal[3] = (byte) (retVal[0]^retVal[1]^retVal[2]);
        }
        return retVal;
    }

    static boolean addressCheck(int address, boolean longAddr) {
        if (address < 0 ) {  // zero is valid broadcast
            log.error("invalid address "+address);
            return false;
        }
        if (longAddr&& (address> (255+(231-192)*256)) ) {
            log.error("invalid address "+address);
            return false;
        }
        if (!longAddr&& (address> 127) ) {
            log.error("invalid address "+address);
            return false;
        }
        return true;  // passes test, hence OK
    }

    static final public int NO_ADDRESS = 1;
    static final public int LOCO_SHORT_ADDRESS = 2;
    static final public int LOCO_LONG_ADDRESS = 4;
    static final public int ACCESSORY_SHORT_ADDRESS = 8;

    /**
     * Extract the address type from an NMRA packet.
     *<P>
     * This finds and returns the type of address within a specific
     * packet, e.g. "the stationary decoder space".
     */
    static int extractAddressType(byte[] packet) {
        return 0;
    }

    /**
     * Extract the numerical address from an NMRA packet.
     *<P>
     * This finds and returns the numerical address within a specific
     * type, e.g. "first address within the stationary decoder space".
     */
    static int extractAddressNumber(byte[] packet) {
        return 0;
    }

     /**
     * Extract the instruction from an NMRA packet
     *<P>
     * This finds and returns the instruction bits within a specific
     * type of packet/instruction, masking off the other bits.
     *
     */
    static int extractInstruction(byte[] packet) {
        return 0;
    }

    /**
     * Convert NMRA packet to a readable form
     */
    static public String format(byte[] p) {
        return jmri.util.StringUtil.hexStringFromBytes(p);
    }

    /**
     * Objects of this class should not be created.
     */

    private NmraPacket() {}
   static org.apache.log4j.Category log = org.apache.log4j.Category.getInstance(NmraPacket.class.getName());
}


/* @(#)NmraPacket.java */

