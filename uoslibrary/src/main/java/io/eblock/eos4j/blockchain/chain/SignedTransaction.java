package io.eblock.eos4j.blockchain.chain;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

import io.eblock.eos4j.blockchain.cypto.digest.Sha256;
import io.eblock.eos4j.blockchain.cypto.ec.EcDsa;
import io.eblock.eos4j.blockchain.cypto.ec.EcSignature;
import io.eblock.eos4j.blockchain.cypto.ec.UosPrivateKey;
import io.eblock.eos4j.blockchain.types.EosByteWriter;
import io.eblock.eos4j.blockchain.types.TypeChainId;
import io.eblock.eos4j.utils.LogUtils;

/**
 * Created by xuhuixiang@ulord.net on 2017-09-12.
 */

public class SignedTransaction extends Transaction {

    @Expose
    private List<String> signatures = null;

    @Expose
    private List<String> context_free_data = new ArrayList<>();


    public SignedTransaction(){
        super();
    }

    public SignedTransaction( SignedTransaction anotherTxn){
        super(anotherTxn);
        this.signatures = deepCopyOnlyContainer( anotherTxn.signatures );
        this.context_free_data = context_free_data;
    }

    public List<String> getSignatures() {
        return signatures;
    }

    public void putSignatures(List<String> signatures) {
        this.signatures = signatures;
    }

    public int getCtxFreeDataCount() {
        return ( context_free_data == null ) ? 0 : context_free_data.size();
    }

    public List<String> getCtxFreeData() {
        return context_free_data;
    }


    private Sha256 getDigestForSignature(TypeChainId chainId) {
        EosByteWriter writer = new EosByteWriter(255);

        // data layout to sign :
        // [ {chainId}, {Transaction( parent class )}, {hash of context_free_data only when exists ]

        writer.putBytes(chainId.getBytes());
        pack( writer);
        if (context_free_data.size() > 0 ) {
        }
        else {
            writer.putBytes( Sha256.ZERO_HASH.getBytes());
        }
        LogUtils.i("333333进来了333333333330000");

        return Sha256.from(writer.toBytes());
    }

    public void sign(UosPrivateKey privateKey, TypeChainId chainId) {
        LogUtils.i("111111进来了333333333330000");

        if ( null == this.signatures){
            this.signatures = new ArrayList<>();
        }
        LogUtils.i("2222222进来了333333333330000");

        EcSignature signature = EcDsa.sign(getDigestForSignature( chainId ), privateKey);
        this.signatures.add( signature.toString());
    }
//    public void sign(EosPrivateKey privateKey, byte[] data) {
//        LogUtils.i("111111进来了333333333330000");
//
//        if ( null == this.signatures){
//            this.signatures = new ArrayList<>();
//        }
//        LogUtils.i("2222222进来了333333333330000");
//
//        EcSignature signature = EcDsa.sign(data, privateKey);
//        this.signatures.add( signature.toString());
//    }
}

