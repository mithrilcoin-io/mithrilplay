/*
 * Copyright (c) 2017 Mithril coin.
 *
 * The MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.mithrilcoin.eoscommander.crypto.ec;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Arrays;

import io.mithrilcoin.eoscommander.crypto.digest.Sha256;
import io.mithrilcoin.eoscommander.crypto.util.Base58;


/**
 * Created by swapnibble on 2017-09-25.
 *
 * 참고:
 * https://github.com/EOSIO/eosjs-ecc
 * https://github.com/EOSIO/eosjs-ecc/blob/master/src/ecdsa.js
 */

public class EosPrivateKey {

    private final BigInteger mPrivateKey;
    private final EosPublicKey mPublicKey;


    private static final SecureRandom mSecRandom;

    static {
        mSecRandom = new SecureRandom();
    }

    public static SecureRandom getSecuRandom(){
        return mSecRandom;
    }

    public EosPrivateKey(){
        int nBitLength = Secp256k1Param.n.bitLength();
        BigInteger d;
        do {
            // Make a BigInteger from bytes to ensure that Android and 'classic'
            // java make the same BigIntegers from the same random source with the
            // same seed. Using BigInteger(nBitLength, random)
            // produces different results on Android compared to 'classic' java.
            byte[] bytes = new byte[nBitLength / 8];
            mSecRandom.nextBytes(bytes);
            bytes[0] = (byte) (bytes[0] & 0x7F); // ensure positive number
            d = new BigInteger(bytes);
        } while (d.equals(BigInteger.ZERO) || (d.compareTo(Secp256k1Param.n) >= 0));

        mPrivateKey = d;

        mPublicKey = new EosPublicKey( findPubKey( d ));
    }

    public EosPrivateKey(byte[] privBytes){
        mPrivateKey = toUnsignedBigInteger( privBytes);

        mPublicKey = new EosPublicKey( findPubKey( mPrivateKey ));
    }

    public EosPrivateKey( String wif ) {
        // 1. Base58 --> raw 로..
        byte[] wifAsRaw = Base58.decode( wif );
        if ( ( null == wifAsRaw) || (wifAsRaw.length < 5 )) {
            throw new IllegalArgumentException("Invalid wif length");
        }

        // offset 0은 제외, 뒤의 4바이트 제외하고, private key 를 뽑자
        Sha256 checkOne = Sha256.from( wifAsRaw, 0, wifAsRaw.length - 4 );
        Sha256 checkTwo = Sha256.from( checkOne.getBytes() );

        // check 값이 맞는지 보자.  원본 eos 소스 코드에서 sha256 1번한 hash 도 체크하게 되어 있다..
        if ( checkTwo.equalsFromOffset( wifAsRaw, wifAsRaw.length - 4, 4)
                || checkOne.equalsFromOffset( wifAsRaw, wifAsRaw.length - 4, 4) ) {

            // ECKey 로 만들어 주자.
            mPrivateKey = toUnsignedBigInteger( Arrays.copyOfRange(wifAsRaw, 1, wifAsRaw.length - 4));
        }
        else {
            throw new IllegalArgumentException("Invalid wif format");
        }

        mPublicKey = new EosPublicKey( findPubKey( toUnsignedBigInteger( mPrivateKey) ));
    }

    public void clear(){
        mPrivateKey.multiply( BigInteger.ZERO );
    }

    private byte[] findPubKey(BigInteger bnum) {
        EcPoint Q = EcTools.multiply(Secp256k1Param.G, bnum);

        // Q를 curve 상에서, compressed point 로 변환하자. ( 압축을 위해 )
        Q = new EcPoint(Q.getCurve(), Q.getX(), Q.getY(), true);

        return Q.getEncoded();
    }

    public EosPublicKey getPublicKey() {
        return mPublicKey;
    }

    public String toWif() {
        byte[] rawPrivKey = getBytes();
        byte[] resultWIFBytes = new byte[ 1 + 32 + 4 ];

        resultWIFBytes[0] = (byte)0x80;
        System.arraycopy( rawPrivKey, rawPrivKey.length > 32 ? 1 : 0, resultWIFBytes, 1 , 32);

        Sha256 hash = Sha256.doubleHash( resultWIFBytes, 0, 33 );

        System.arraycopy( hash.getBytes(), 0, resultWIFBytes, 33, 4 );

        return Base58.encode( resultWIFBytes );
    }

    @Override
    public String toString() {
        return toWif();
    }

    public BigInteger getAsBigInteger() {
        return mPrivateKey;
    }


    public byte[] getBytes() {
        byte[] result = new byte[32];
        byte[] bytes = mPrivateKey.toByteArray();
        if (bytes.length <= result.length) {
            System.arraycopy(bytes, 0, result, result.length - bytes.length, bytes.length);
        } else {
            assert bytes.length == 33 && bytes[0] == 0;
            System.arraycopy(bytes, 1, result, 0, bytes.length - 1);
        }
        return result;
    }

    public byte[] getBytes(BigInteger value) {
        byte[] result = new byte[32];
        byte[] bytes = value.toByteArray();
        if (bytes.length <= result.length) {
            System.arraycopy(bytes, 0, result, result.length - bytes.length, bytes.length);
        } else {
            // This happens if the most significant bit is set and we have an
            // extra leading zero to avoid a negative BigInteger
            assert bytes.length == 33 && bytes[0] == 0;
            System.arraycopy(bytes, 1, result, 0, bytes.length - 1);
        }
        return result;
    }

    private BigInteger toUnsignedBigInteger(BigInteger value ) {
        if ( value.signum() < 0 ) {
            return new BigInteger( 1, value.toByteArray());
        }

        return value;
    }

    private BigInteger toUnsignedBigInteger(byte[] value ) {
        if ( (( value[0]) & 0x80) != 0 ) {
            return new BigInteger( 1, value);
        }

        return new BigInteger(value);
    }
}
