/**
 * Autogenerated by Avro
 * 
 * DO NOT EDIT DIRECTLY
 */
package com.duowan.gamebox.msgcenter.serialize.util;  
@SuppressWarnings("all")
@org.apache.avro.specific.AvroGenerated
public class InvitationDetailAvro extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"InvitationDetailAvro\",\"namespace\":\"com.duowan.gamebox.msgcenter.serialize.util\",\"fields\":[{\"name\":\"serverUrl\",\"type\":[\"string\",\"null\"]},{\"name\":\"inviteId\",\"type\":\"string\"},{\"name\":\"toUserId\",\"type\":[\"string\",\"null\"]},{\"name\":\"toUserType\",\"type\":[\"string\",\"null\"]},{\"name\":\"toDisplayName\",\"type\":[\"string\",\"null\"]},{\"name\":\"replyTimestamp\",\"type\":[\"long\",\"null\"]},{\"name\":\"replyTag\",\"type\":[\"int\",\"null\"]},{\"name\":\"notifyReplyTimestamp\",\"type\":[\"long\",\"null\"]},{\"name\":\"notifyReplyTag\",\"type\":[\"int\",\"null\"]},{\"name\":\"replyExtra\",\"type\":[\"string\",\"null\"]},{\"name\":\"toTimestamp\",\"type\":[\"long\",\"null\"]},{\"name\":\"toTag\",\"type\":[\"int\",\"null\"]}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }
   private java.lang.CharSequence serverUrl;
   private java.lang.CharSequence inviteId;
   private java.lang.CharSequence toUserId;
   private java.lang.CharSequence toUserType;
   private java.lang.CharSequence toDisplayName;
   private java.lang.Long replyTimestamp;
   private java.lang.Integer replyTag;
   private java.lang.Long notifyReplyTimestamp;
   private java.lang.Integer notifyReplyTag;
   private java.lang.CharSequence replyExtra;
   private java.lang.Long toTimestamp;
   private java.lang.Integer toTag;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use {@link \#newBuilder()}. 
   */
  public InvitationDetailAvro() {}

  /**
   * All-args constructor.
   */
  public InvitationDetailAvro(java.lang.CharSequence serverUrl, java.lang.CharSequence inviteId, java.lang.CharSequence toUserId, java.lang.CharSequence toUserType, java.lang.CharSequence toDisplayName, java.lang.Long replyTimestamp, java.lang.Integer replyTag, java.lang.Long notifyReplyTimestamp, java.lang.Integer notifyReplyTag, java.lang.CharSequence replyExtra, java.lang.Long toTimestamp, java.lang.Integer toTag) {
    this.serverUrl = serverUrl;
    this.inviteId = inviteId;
    this.toUserId = toUserId;
    this.toUserType = toUserType;
    this.toDisplayName = toDisplayName;
    this.replyTimestamp = replyTimestamp;
    this.replyTag = replyTag;
    this.notifyReplyTimestamp = notifyReplyTimestamp;
    this.notifyReplyTag = notifyReplyTag;
    this.replyExtra = replyExtra;
    this.toTimestamp = toTimestamp;
    this.toTag = toTag;
  }

  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call. 
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return serverUrl;
    case 1: return inviteId;
    case 2: return toUserId;
    case 3: return toUserType;
    case 4: return toDisplayName;
    case 5: return replyTimestamp;
    case 6: return replyTag;
    case 7: return notifyReplyTimestamp;
    case 8: return notifyReplyTag;
    case 9: return replyExtra;
    case 10: return toTimestamp;
    case 11: return toTag;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }
  // Used by DatumReader.  Applications should not call. 
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: serverUrl = (java.lang.CharSequence)value$; break;
    case 1: inviteId = (java.lang.CharSequence)value$; break;
    case 2: toUserId = (java.lang.CharSequence)value$; break;
    case 3: toUserType = (java.lang.CharSequence)value$; break;
    case 4: toDisplayName = (java.lang.CharSequence)value$; break;
    case 5: replyTimestamp = (java.lang.Long)value$; break;
    case 6: replyTag = (java.lang.Integer)value$; break;
    case 7: notifyReplyTimestamp = (java.lang.Long)value$; break;
    case 8: notifyReplyTag = (java.lang.Integer)value$; break;
    case 9: replyExtra = (java.lang.CharSequence)value$; break;
    case 10: toTimestamp = (java.lang.Long)value$; break;
    case 11: toTag = (java.lang.Integer)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'serverUrl' field.
   */
  public java.lang.CharSequence getServerUrl() {
    return serverUrl;
  }

  /**
   * Sets the value of the 'serverUrl' field.
   * @param value the value to set.
   */
  public void setServerUrl(java.lang.CharSequence value) {
    this.serverUrl = value;
  }

  /**
   * Gets the value of the 'inviteId' field.
   */
  public java.lang.CharSequence getInviteId() {
    return inviteId;
  }

  /**
   * Sets the value of the 'inviteId' field.
   * @param value the value to set.
   */
  public void setInviteId(java.lang.CharSequence value) {
    this.inviteId = value;
  }

  /**
   * Gets the value of the 'toUserId' field.
   */
  public java.lang.CharSequence getToUserId() {
    return toUserId;
  }

  /**
   * Sets the value of the 'toUserId' field.
   * @param value the value to set.
   */
  public void setToUserId(java.lang.CharSequence value) {
    this.toUserId = value;
  }

  /**
   * Gets the value of the 'toUserType' field.
   */
  public java.lang.CharSequence getToUserType() {
    return toUserType;
  }

  /**
   * Sets the value of the 'toUserType' field.
   * @param value the value to set.
   */
  public void setToUserType(java.lang.CharSequence value) {
    this.toUserType = value;
  }

  /**
   * Gets the value of the 'toDisplayName' field.
   */
  public java.lang.CharSequence getToDisplayName() {
    return toDisplayName;
  }

  /**
   * Sets the value of the 'toDisplayName' field.
   * @param value the value to set.
   */
  public void setToDisplayName(java.lang.CharSequence value) {
    this.toDisplayName = value;
  }

  /**
   * Gets the value of the 'replyTimestamp' field.
   */
  public java.lang.Long getReplyTimestamp() {
    return replyTimestamp;
  }

  /**
   * Sets the value of the 'replyTimestamp' field.
   * @param value the value to set.
   */
  public void setReplyTimestamp(java.lang.Long value) {
    this.replyTimestamp = value;
  }

  /**
   * Gets the value of the 'replyTag' field.
   */
  public java.lang.Integer getReplyTag() {
    return replyTag;
  }

  /**
   * Sets the value of the 'replyTag' field.
   * @param value the value to set.
   */
  public void setReplyTag(java.lang.Integer value) {
    this.replyTag = value;
  }

  /**
   * Gets the value of the 'notifyReplyTimestamp' field.
   */
  public java.lang.Long getNotifyReplyTimestamp() {
    return notifyReplyTimestamp;
  }

  /**
   * Sets the value of the 'notifyReplyTimestamp' field.
   * @param value the value to set.
   */
  public void setNotifyReplyTimestamp(java.lang.Long value) {
    this.notifyReplyTimestamp = value;
  }

  /**
   * Gets the value of the 'notifyReplyTag' field.
   */
  public java.lang.Integer getNotifyReplyTag() {
    return notifyReplyTag;
  }

  /**
   * Sets the value of the 'notifyReplyTag' field.
   * @param value the value to set.
   */
  public void setNotifyReplyTag(java.lang.Integer value) {
    this.notifyReplyTag = value;
  }

  /**
   * Gets the value of the 'replyExtra' field.
   */
  public java.lang.CharSequence getReplyExtra() {
    return replyExtra;
  }

  /**
   * Sets the value of the 'replyExtra' field.
   * @param value the value to set.
   */
  public void setReplyExtra(java.lang.CharSequence value) {
    this.replyExtra = value;
  }

  /**
   * Gets the value of the 'toTimestamp' field.
   */
  public java.lang.Long getToTimestamp() {
    return toTimestamp;
  }

  /**
   * Sets the value of the 'toTimestamp' field.
   * @param value the value to set.
   */
  public void setToTimestamp(java.lang.Long value) {
    this.toTimestamp = value;
  }

  /**
   * Gets the value of the 'toTag' field.
   */
  public java.lang.Integer getToTag() {
    return toTag;
  }

  /**
   * Sets the value of the 'toTag' field.
   * @param value the value to set.
   */
  public void setToTag(java.lang.Integer value) {
    this.toTag = value;
  }

  /** Creates a new InvitationDetailAvro RecordBuilder */
  public static com.duowan.gamebox.msgcenter.serialize.util.InvitationDetailAvro.Builder newBuilder() {
    return new com.duowan.gamebox.msgcenter.serialize.util.InvitationDetailAvro.Builder();
  }
  
  /** Creates a new InvitationDetailAvro RecordBuilder by copying an existing Builder */
  public static com.duowan.gamebox.msgcenter.serialize.util.InvitationDetailAvro.Builder newBuilder(com.duowan.gamebox.msgcenter.serialize.util.InvitationDetailAvro.Builder other) {
    return new com.duowan.gamebox.msgcenter.serialize.util.InvitationDetailAvro.Builder(other);
  }
  
  /** Creates a new InvitationDetailAvro RecordBuilder by copying an existing InvitationDetailAvro instance */
  public static com.duowan.gamebox.msgcenter.serialize.util.InvitationDetailAvro.Builder newBuilder(com.duowan.gamebox.msgcenter.serialize.util.InvitationDetailAvro other) {
    return new com.duowan.gamebox.msgcenter.serialize.util.InvitationDetailAvro.Builder(other);
  }
  
  /**
   * RecordBuilder for InvitationDetailAvro instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<InvitationDetailAvro>
    implements org.apache.avro.data.RecordBuilder<InvitationDetailAvro> {

    private java.lang.CharSequence serverUrl;
    private java.lang.CharSequence inviteId;
    private java.lang.CharSequence toUserId;
    private java.lang.CharSequence toUserType;
    private java.lang.CharSequence toDisplayName;
    private java.lang.Long replyTimestamp;
    private java.lang.Integer replyTag;
    private java.lang.Long notifyReplyTimestamp;
    private java.lang.Integer notifyReplyTag;
    private java.lang.CharSequence replyExtra;
    private java.lang.Long toTimestamp;
    private java.lang.Integer toTag;

    /** Creates a new Builder */
    private Builder() {
      super(com.duowan.gamebox.msgcenter.serialize.util.InvitationDetailAvro.SCHEMA$);
    }
    
    /** Creates a Builder by copying an existing Builder */
    private Builder(com.duowan.gamebox.msgcenter.serialize.util.InvitationDetailAvro.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.serverUrl)) {
        this.serverUrl = data().deepCopy(fields()[0].schema(), other.serverUrl);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.inviteId)) {
        this.inviteId = data().deepCopy(fields()[1].schema(), other.inviteId);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.toUserId)) {
        this.toUserId = data().deepCopy(fields()[2].schema(), other.toUserId);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.toUserType)) {
        this.toUserType = data().deepCopy(fields()[3].schema(), other.toUserType);
        fieldSetFlags()[3] = true;
      }
      if (isValidValue(fields()[4], other.toDisplayName)) {
        this.toDisplayName = data().deepCopy(fields()[4].schema(), other.toDisplayName);
        fieldSetFlags()[4] = true;
      }
      if (isValidValue(fields()[5], other.replyTimestamp)) {
        this.replyTimestamp = data().deepCopy(fields()[5].schema(), other.replyTimestamp);
        fieldSetFlags()[5] = true;
      }
      if (isValidValue(fields()[6], other.replyTag)) {
        this.replyTag = data().deepCopy(fields()[6].schema(), other.replyTag);
        fieldSetFlags()[6] = true;
      }
      if (isValidValue(fields()[7], other.notifyReplyTimestamp)) {
        this.notifyReplyTimestamp = data().deepCopy(fields()[7].schema(), other.notifyReplyTimestamp);
        fieldSetFlags()[7] = true;
      }
      if (isValidValue(fields()[8], other.notifyReplyTag)) {
        this.notifyReplyTag = data().deepCopy(fields()[8].schema(), other.notifyReplyTag);
        fieldSetFlags()[8] = true;
      }
      if (isValidValue(fields()[9], other.replyExtra)) {
        this.replyExtra = data().deepCopy(fields()[9].schema(), other.replyExtra);
        fieldSetFlags()[9] = true;
      }
      if (isValidValue(fields()[10], other.toTimestamp)) {
        this.toTimestamp = data().deepCopy(fields()[10].schema(), other.toTimestamp);
        fieldSetFlags()[10] = true;
      }
      if (isValidValue(fields()[11], other.toTag)) {
        this.toTag = data().deepCopy(fields()[11].schema(), other.toTag);
        fieldSetFlags()[11] = true;
      }
    }
    
    /** Creates a Builder by copying an existing InvitationDetailAvro instance */
    private Builder(com.duowan.gamebox.msgcenter.serialize.util.InvitationDetailAvro other) {
            super(com.duowan.gamebox.msgcenter.serialize.util.InvitationDetailAvro.SCHEMA$);
      if (isValidValue(fields()[0], other.serverUrl)) {
        this.serverUrl = data().deepCopy(fields()[0].schema(), other.serverUrl);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.inviteId)) {
        this.inviteId = data().deepCopy(fields()[1].schema(), other.inviteId);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.toUserId)) {
        this.toUserId = data().deepCopy(fields()[2].schema(), other.toUserId);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.toUserType)) {
        this.toUserType = data().deepCopy(fields()[3].schema(), other.toUserType);
        fieldSetFlags()[3] = true;
      }
      if (isValidValue(fields()[4], other.toDisplayName)) {
        this.toDisplayName = data().deepCopy(fields()[4].schema(), other.toDisplayName);
        fieldSetFlags()[4] = true;
      }
      if (isValidValue(fields()[5], other.replyTimestamp)) {
        this.replyTimestamp = data().deepCopy(fields()[5].schema(), other.replyTimestamp);
        fieldSetFlags()[5] = true;
      }
      if (isValidValue(fields()[6], other.replyTag)) {
        this.replyTag = data().deepCopy(fields()[6].schema(), other.replyTag);
        fieldSetFlags()[6] = true;
      }
      if (isValidValue(fields()[7], other.notifyReplyTimestamp)) {
        this.notifyReplyTimestamp = data().deepCopy(fields()[7].schema(), other.notifyReplyTimestamp);
        fieldSetFlags()[7] = true;
      }
      if (isValidValue(fields()[8], other.notifyReplyTag)) {
        this.notifyReplyTag = data().deepCopy(fields()[8].schema(), other.notifyReplyTag);
        fieldSetFlags()[8] = true;
      }
      if (isValidValue(fields()[9], other.replyExtra)) {
        this.replyExtra = data().deepCopy(fields()[9].schema(), other.replyExtra);
        fieldSetFlags()[9] = true;
      }
      if (isValidValue(fields()[10], other.toTimestamp)) {
        this.toTimestamp = data().deepCopy(fields()[10].schema(), other.toTimestamp);
        fieldSetFlags()[10] = true;
      }
      if (isValidValue(fields()[11], other.toTag)) {
        this.toTag = data().deepCopy(fields()[11].schema(), other.toTag);
        fieldSetFlags()[11] = true;
      }
    }

    /** Gets the value of the 'serverUrl' field */
    public java.lang.CharSequence getServerUrl() {
      return serverUrl;
    }
    
    /** Sets the value of the 'serverUrl' field */
    public com.duowan.gamebox.msgcenter.serialize.util.InvitationDetailAvro.Builder setServerUrl(java.lang.CharSequence value) {
      validate(fields()[0], value);
      this.serverUrl = value;
      fieldSetFlags()[0] = true;
      return this; 
    }
    
    /** Checks whether the 'serverUrl' field has been set */
    public boolean hasServerUrl() {
      return fieldSetFlags()[0];
    }
    
    /** Clears the value of the 'serverUrl' field */
    public com.duowan.gamebox.msgcenter.serialize.util.InvitationDetailAvro.Builder clearServerUrl() {
      serverUrl = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /** Gets the value of the 'inviteId' field */
    public java.lang.CharSequence getInviteId() {
      return inviteId;
    }
    
    /** Sets the value of the 'inviteId' field */
    public com.duowan.gamebox.msgcenter.serialize.util.InvitationDetailAvro.Builder setInviteId(java.lang.CharSequence value) {
      validate(fields()[1], value);
      this.inviteId = value;
      fieldSetFlags()[1] = true;
      return this; 
    }
    
    /** Checks whether the 'inviteId' field has been set */
    public boolean hasInviteId() {
      return fieldSetFlags()[1];
    }
    
    /** Clears the value of the 'inviteId' field */
    public com.duowan.gamebox.msgcenter.serialize.util.InvitationDetailAvro.Builder clearInviteId() {
      inviteId = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    /** Gets the value of the 'toUserId' field */
    public java.lang.CharSequence getToUserId() {
      return toUserId;
    }
    
    /** Sets the value of the 'toUserId' field */
    public com.duowan.gamebox.msgcenter.serialize.util.InvitationDetailAvro.Builder setToUserId(java.lang.CharSequence value) {
      validate(fields()[2], value);
      this.toUserId = value;
      fieldSetFlags()[2] = true;
      return this; 
    }
    
    /** Checks whether the 'toUserId' field has been set */
    public boolean hasToUserId() {
      return fieldSetFlags()[2];
    }
    
    /** Clears the value of the 'toUserId' field */
    public com.duowan.gamebox.msgcenter.serialize.util.InvitationDetailAvro.Builder clearToUserId() {
      toUserId = null;
      fieldSetFlags()[2] = false;
      return this;
    }

    /** Gets the value of the 'toUserType' field */
    public java.lang.CharSequence getToUserType() {
      return toUserType;
    }
    
    /** Sets the value of the 'toUserType' field */
    public com.duowan.gamebox.msgcenter.serialize.util.InvitationDetailAvro.Builder setToUserType(java.lang.CharSequence value) {
      validate(fields()[3], value);
      this.toUserType = value;
      fieldSetFlags()[3] = true;
      return this; 
    }
    
    /** Checks whether the 'toUserType' field has been set */
    public boolean hasToUserType() {
      return fieldSetFlags()[3];
    }
    
    /** Clears the value of the 'toUserType' field */
    public com.duowan.gamebox.msgcenter.serialize.util.InvitationDetailAvro.Builder clearToUserType() {
      toUserType = null;
      fieldSetFlags()[3] = false;
      return this;
    }

    /** Gets the value of the 'toDisplayName' field */
    public java.lang.CharSequence getToDisplayName() {
      return toDisplayName;
    }
    
    /** Sets the value of the 'toDisplayName' field */
    public com.duowan.gamebox.msgcenter.serialize.util.InvitationDetailAvro.Builder setToDisplayName(java.lang.CharSequence value) {
      validate(fields()[4], value);
      this.toDisplayName = value;
      fieldSetFlags()[4] = true;
      return this; 
    }
    
    /** Checks whether the 'toDisplayName' field has been set */
    public boolean hasToDisplayName() {
      return fieldSetFlags()[4];
    }
    
    /** Clears the value of the 'toDisplayName' field */
    public com.duowan.gamebox.msgcenter.serialize.util.InvitationDetailAvro.Builder clearToDisplayName() {
      toDisplayName = null;
      fieldSetFlags()[4] = false;
      return this;
    }

    /** Gets the value of the 'replyTimestamp' field */
    public java.lang.Long getReplyTimestamp() {
      return replyTimestamp;
    }
    
    /** Sets the value of the 'replyTimestamp' field */
    public com.duowan.gamebox.msgcenter.serialize.util.InvitationDetailAvro.Builder setReplyTimestamp(java.lang.Long value) {
      validate(fields()[5], value);
      this.replyTimestamp = value;
      fieldSetFlags()[5] = true;
      return this; 
    }
    
    /** Checks whether the 'replyTimestamp' field has been set */
    public boolean hasReplyTimestamp() {
      return fieldSetFlags()[5];
    }
    
    /** Clears the value of the 'replyTimestamp' field */
    public com.duowan.gamebox.msgcenter.serialize.util.InvitationDetailAvro.Builder clearReplyTimestamp() {
      replyTimestamp = null;
      fieldSetFlags()[5] = false;
      return this;
    }

    /** Gets the value of the 'replyTag' field */
    public java.lang.Integer getReplyTag() {
      return replyTag;
    }
    
    /** Sets the value of the 'replyTag' field */
    public com.duowan.gamebox.msgcenter.serialize.util.InvitationDetailAvro.Builder setReplyTag(java.lang.Integer value) {
      validate(fields()[6], value);
      this.replyTag = value;
      fieldSetFlags()[6] = true;
      return this; 
    }
    
    /** Checks whether the 'replyTag' field has been set */
    public boolean hasReplyTag() {
      return fieldSetFlags()[6];
    }
    
    /** Clears the value of the 'replyTag' field */
    public com.duowan.gamebox.msgcenter.serialize.util.InvitationDetailAvro.Builder clearReplyTag() {
      replyTag = null;
      fieldSetFlags()[6] = false;
      return this;
    }

    /** Gets the value of the 'notifyReplyTimestamp' field */
    public java.lang.Long getNotifyReplyTimestamp() {
      return notifyReplyTimestamp;
    }
    
    /** Sets the value of the 'notifyReplyTimestamp' field */
    public com.duowan.gamebox.msgcenter.serialize.util.InvitationDetailAvro.Builder setNotifyReplyTimestamp(java.lang.Long value) {
      validate(fields()[7], value);
      this.notifyReplyTimestamp = value;
      fieldSetFlags()[7] = true;
      return this; 
    }
    
    /** Checks whether the 'notifyReplyTimestamp' field has been set */
    public boolean hasNotifyReplyTimestamp() {
      return fieldSetFlags()[7];
    }
    
    /** Clears the value of the 'notifyReplyTimestamp' field */
    public com.duowan.gamebox.msgcenter.serialize.util.InvitationDetailAvro.Builder clearNotifyReplyTimestamp() {
      notifyReplyTimestamp = null;
      fieldSetFlags()[7] = false;
      return this;
    }

    /** Gets the value of the 'notifyReplyTag' field */
    public java.lang.Integer getNotifyReplyTag() {
      return notifyReplyTag;
    }
    
    /** Sets the value of the 'notifyReplyTag' field */
    public com.duowan.gamebox.msgcenter.serialize.util.InvitationDetailAvro.Builder setNotifyReplyTag(java.lang.Integer value) {
      validate(fields()[8], value);
      this.notifyReplyTag = value;
      fieldSetFlags()[8] = true;
      return this; 
    }
    
    /** Checks whether the 'notifyReplyTag' field has been set */
    public boolean hasNotifyReplyTag() {
      return fieldSetFlags()[8];
    }
    
    /** Clears the value of the 'notifyReplyTag' field */
    public com.duowan.gamebox.msgcenter.serialize.util.InvitationDetailAvro.Builder clearNotifyReplyTag() {
      notifyReplyTag = null;
      fieldSetFlags()[8] = false;
      return this;
    }

    /** Gets the value of the 'replyExtra' field */
    public java.lang.CharSequence getReplyExtra() {
      return replyExtra;
    }
    
    /** Sets the value of the 'replyExtra' field */
    public com.duowan.gamebox.msgcenter.serialize.util.InvitationDetailAvro.Builder setReplyExtra(java.lang.CharSequence value) {
      validate(fields()[9], value);
      this.replyExtra = value;
      fieldSetFlags()[9] = true;
      return this; 
    }
    
    /** Checks whether the 'replyExtra' field has been set */
    public boolean hasReplyExtra() {
      return fieldSetFlags()[9];
    }
    
    /** Clears the value of the 'replyExtra' field */
    public com.duowan.gamebox.msgcenter.serialize.util.InvitationDetailAvro.Builder clearReplyExtra() {
      replyExtra = null;
      fieldSetFlags()[9] = false;
      return this;
    }

    /** Gets the value of the 'toTimestamp' field */
    public java.lang.Long getToTimestamp() {
      return toTimestamp;
    }
    
    /** Sets the value of the 'toTimestamp' field */
    public com.duowan.gamebox.msgcenter.serialize.util.InvitationDetailAvro.Builder setToTimestamp(java.lang.Long value) {
      validate(fields()[10], value);
      this.toTimestamp = value;
      fieldSetFlags()[10] = true;
      return this; 
    }
    
    /** Checks whether the 'toTimestamp' field has been set */
    public boolean hasToTimestamp() {
      return fieldSetFlags()[10];
    }
    
    /** Clears the value of the 'toTimestamp' field */
    public com.duowan.gamebox.msgcenter.serialize.util.InvitationDetailAvro.Builder clearToTimestamp() {
      toTimestamp = null;
      fieldSetFlags()[10] = false;
      return this;
    }

    /** Gets the value of the 'toTag' field */
    public java.lang.Integer getToTag() {
      return toTag;
    }
    
    /** Sets the value of the 'toTag' field */
    public com.duowan.gamebox.msgcenter.serialize.util.InvitationDetailAvro.Builder setToTag(java.lang.Integer value) {
      validate(fields()[11], value);
      this.toTag = value;
      fieldSetFlags()[11] = true;
      return this; 
    }
    
    /** Checks whether the 'toTag' field has been set */
    public boolean hasToTag() {
      return fieldSetFlags()[11];
    }
    
    /** Clears the value of the 'toTag' field */
    public com.duowan.gamebox.msgcenter.serialize.util.InvitationDetailAvro.Builder clearToTag() {
      toTag = null;
      fieldSetFlags()[11] = false;
      return this;
    }

    @Override
    public InvitationDetailAvro build() {
      try {
        InvitationDetailAvro record = new InvitationDetailAvro();
        record.serverUrl = fieldSetFlags()[0] ? this.serverUrl : (java.lang.CharSequence) defaultValue(fields()[0]);
        record.inviteId = fieldSetFlags()[1] ? this.inviteId : (java.lang.CharSequence) defaultValue(fields()[1]);
        record.toUserId = fieldSetFlags()[2] ? this.toUserId : (java.lang.CharSequence) defaultValue(fields()[2]);
        record.toUserType = fieldSetFlags()[3] ? this.toUserType : (java.lang.CharSequence) defaultValue(fields()[3]);
        record.toDisplayName = fieldSetFlags()[4] ? this.toDisplayName : (java.lang.CharSequence) defaultValue(fields()[4]);
        record.replyTimestamp = fieldSetFlags()[5] ? this.replyTimestamp : (java.lang.Long) defaultValue(fields()[5]);
        record.replyTag = fieldSetFlags()[6] ? this.replyTag : (java.lang.Integer) defaultValue(fields()[6]);
        record.notifyReplyTimestamp = fieldSetFlags()[7] ? this.notifyReplyTimestamp : (java.lang.Long) defaultValue(fields()[7]);
        record.notifyReplyTag = fieldSetFlags()[8] ? this.notifyReplyTag : (java.lang.Integer) defaultValue(fields()[8]);
        record.replyExtra = fieldSetFlags()[9] ? this.replyExtra : (java.lang.CharSequence) defaultValue(fields()[9]);
        record.toTimestamp = fieldSetFlags()[10] ? this.toTimestamp : (java.lang.Long) defaultValue(fields()[10]);
        record.toTag = fieldSetFlags()[11] ? this.toTag : (java.lang.Integer) defaultValue(fields()[11]);
        return record;
      } catch (Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }
}