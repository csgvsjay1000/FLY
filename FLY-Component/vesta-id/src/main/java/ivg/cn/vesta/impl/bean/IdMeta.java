package ivg.cn.vesta.impl.bean;

public class IdMeta {
	private byte machineBits;

    private byte seqBits;

    private byte timeBits;

    private byte genMethodBits;

    private byte typeBits;

    private byte versionBits;
    
    public IdMeta(byte machineBits, byte seqBits, byte timeBits, byte genMethodBits, byte typeBits, byte versionBits) {
        super();

        this.machineBits = machineBits;
        this.seqBits = seqBits;
        this.timeBits = timeBits;
        this.genMethodBits = genMethodBits;
        this.typeBits = typeBits;
        this.versionBits = versionBits;
    }

	public byte getMachineBits() {
		return machineBits;
	}

	public void setMachineBits(byte machineBits) {
		this.machineBits = machineBits;
	}

	public byte getSeqBits() {
		return seqBits;
	}

	public void setSeqBits(byte seqBits) {
		this.seqBits = seqBits;
	}

	public byte getTimeBits() {
		return timeBits;
	}

	public void setTimeBits(byte timeBits) {
		this.timeBits = timeBits;
	}

	public byte getGenMethodBits() {
		return genMethodBits;
	}

	public void setGenMethodBits(byte genMethodBits) {
		this.genMethodBits = genMethodBits;
	}

	public byte getTypeBits() {
		return typeBits;
	}

	public void setTypeBits(byte typeBits) {
		this.typeBits = typeBits;
	}

	public byte getVersionBits() {
		return versionBits;
	}

	public void setVersionBits(byte versionBits) {
		this.versionBits = versionBits;
	}
	
	public long getSeqBitsStartPos() {
        return machineBits;
    }
	
	public long getTimeBitsStartPos() {
        return machineBits + seqBits;
    }
	
	public long getGenMethodBitsStartPos() {
        return machineBits + seqBits + timeBits;
    }
	
	public long getTypeBitsStartPos() {
        return machineBits + seqBits + timeBits + genMethodBits;
    }
	
	public long getVersionBitsStartPos() {
        return machineBits + seqBits + timeBits + genMethodBits + typeBits;
    }
	
	/**
	 * <pre>
	 * -1L的二进制 0xFFFF FFFF FFFF FFFF
	 * 若seqBits = 20, -1L << seqBits = 0xFFFF FFFF FFF0 0000
	 * ^ 异或运算 相同为0 不同为1 
	 * -1L ^ -1L << seqBits = 0x0000 0000 000F FFFF
	 * @return  0x0000 0000 000F FFFF
	 * */
	public long getSeqBitsMask() {
		return -1L ^ -1L << seqBits;
	}
	
	public long getMachineBitsMask() {
		return -1L ^ -1L << machineBits;
	}

	public long getTimeBitsMask() {
		return -1L ^ -1L << timeBits;
	}
	
	public long getGenMethodBitsMask() {
		return -1L ^ -1L << genMethodBits;
	}
	
	public long getTypeBitsMask() {
		return -1L ^ -1L << typeBits;
	}
	
	public long getVersionBitsMask() {
		return -1L ^ -1L << versionBits;
	}
	
}
