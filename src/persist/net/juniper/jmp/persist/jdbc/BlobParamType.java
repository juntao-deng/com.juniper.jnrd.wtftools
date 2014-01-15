package net.juniper.jmp.persist.jdbc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;

import net.juniper.jmp.persist.exp.JmpDbRuntimeException;

import org.apache.commons.io.IOUtils;

/**
 * @author juntaod
 *
 */
public class BlobParamType implements SQLParamType {
	private static final long serialVersionUID = -2609854149576556913L;
	private byte bytes[] = null;
    private int length = -1;

    public BlobParamType(Object blob) {
    	readBytes(blob);
    	this.length = bytes.length;
    }

    public BlobParamType(byte[] bytes) {
        this.bytes = bytes;
        this.length = bytes == null ? -1 : bytes.length;
    }

    public BlobParamType(InputStream input, int length) {
        readBytes(input);
        this.length = bytes == null ? -1 : bytes.length;
    }

    private void readBytes(InputStream input) {
    	try {
			this.bytes = IOUtils.toByteArray(input);
		} 
    	catch (IOException e) {
    		throw new JmpDbRuntimeException(e.getMessage(), e);
		}
	}
    
    private void readBytes(Object obj){
        ObjectOutputStream oos = null;
    	try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            oos.flush();
            baos.flush();
            bytes = baos.toByteArray();
        } 
        catch (IOException e) {
            throw new JmpDbRuntimeException(e.getMessage(), e);
        }
    	finally{
    		IOUtils.closeQuietly(oos);
    	}
    }

	public byte[] getBytes() {
        return bytes;
    }

    public int getLength() {
        return length;
    }
}
