// Generated by the protocol buffer compiler. DO NOT EDIT!
// source: Test.proto

package com.xyz.rpc.benchmark;

import com.google.protobuf.UnknownFieldSet;

public final class PB {
  private PB() {
  }

  public static void registerAllExtensions(com.google.protobuf.ExtensionRegistry registry) {
  }

  public interface RequestObjectOrBuilder extends com.google.protobuf.MessageOrBuilder {

    // required bytes bytesObject = 1;
    boolean hasBytesObject();

    com.google.protobuf.ByteString getBytesObject();
  }

  public static final class RequestObject extends com.google.protobuf.GeneratedMessage implements RequestObjectOrBuilder {
    // Use RequestObject.newBuilder() to construct.
    private RequestObject(Builder builder) {
      super(builder);
    }

    private RequestObject(boolean noInit) {
    }

    private static final RequestObject defaultInstance;

    public static RequestObject getDefaultInstance() {
      return defaultInstance;
    }

    public RequestObject getDefaultInstanceForType() {
      return defaultInstance;
    }

    public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
      return com.xyz.rpc.benchmark.PB.internal_static_com_bluedavy_RequestObject_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
      return com.xyz.rpc.benchmark.PB.internal_static_com_bluedavy_RequestObject_fieldAccessorTable;
    }

    private int bitField0_;
    // required bytes bytesObject = 1;
    public static final int BYTESOBJECT_FIELD_NUMBER = 1;
    private com.google.protobuf.ByteString bytesObject_;

    public boolean hasBytesObject() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }

    public com.google.protobuf.ByteString getBytesObject() {
      return bytesObject_;
    }

    private void initFields() {
      bytesObject_ = com.google.protobuf.ByteString.EMPTY;
    }

    private byte memoizedIsInitialized = -1;

    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1)
        return isInitialized == 1;

      if (!hasBytesObject()) {
        memoizedIsInitialized = 0;
        return false;
      }
      memoizedIsInitialized = 1;
      return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output) throws java.io.IOException {
      getSerializedSize();
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeBytes(1, bytesObject_);
      }
      getUnknownFields().writeTo(output);
    }

    @Override
    public UnknownFieldSet getUnknownFields() {
      // TODO Auto-generated method stub
      return UnknownFieldSet.getDefaultInstance();
    }

    private int memoizedSerializedSize = -1;

    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1)
        return size;

      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream.computeBytesSize(1, bytesObject_);
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;

    @java.lang.Override
    protected java.lang.Object writeReplace() throws java.io.ObjectStreamException {
      return super.writeReplace();
    }

    public static com.xyz.rpc.benchmark.PB.RequestObject parseFrom(com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }

    public static com.xyz.rpc.benchmark.PB.RequestObject parseFrom(com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry) throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry).buildParsed();
    }

    public static com.xyz.rpc.benchmark.PB.RequestObject parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }

    public static com.xyz.rpc.benchmark.PB.RequestObject parseFrom(byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry) throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry).buildParsed();
    }

    public static com.xyz.rpc.benchmark.PB.RequestObject parseFrom(java.io.InputStream input) throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }

    public static com.xyz.rpc.benchmark.PB.RequestObject parseFrom(java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry) throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry).buildParsed();
    }

    public static com.xyz.rpc.benchmark.PB.RequestObject parseDelimitedFrom(java.io.InputStream input) throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }

    public static com.xyz.rpc.benchmark.PB.RequestObject parseDelimitedFrom(java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry) throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input, extensionRegistry)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }

    public static com.xyz.rpc.benchmark.PB.RequestObject parseFrom(com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }

    public static com.xyz.rpc.benchmark.PB.RequestObject parseFrom(com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry) throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry).buildParsed();
    }

    public static Builder newBuilder() {
      return Builder.create();
    }

    public Builder newBuilderForType() {
      return newBuilder();
    }

    public static Builder newBuilder(com.xyz.rpc.benchmark.PB.RequestObject prototype) {
      return newBuilder().mergeFrom(prototype);
    }

    public Builder toBuilder() {
      return newBuilder(this);
    }

    @java.lang.Override
    protected Builder newBuilderForType(com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }

    public static final class Builder extends com.google.protobuf.GeneratedMessage.Builder<Builder>
        implements com.xyz.rpc.benchmark.PB.RequestObjectOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
        return com.xyz.rpc.benchmark.PB.internal_static_com_bluedavy_RequestObject_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
        return com.xyz.rpc.benchmark.PB.internal_static_com_bluedavy_RequestObject_fieldAccessorTable;
      }

      // Construct using
      // code.google.nfs.rpc.benchmark.PB.RequestObject.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }

      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
        }
      }

      private static Builder create() {
        return new Builder();
      }

      public Builder clear() {
        super.clear();
        bytesObject_ = com.google.protobuf.ByteString.EMPTY;
        bitField0_ = (bitField0_ & ~0x00000001);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor getDescriptorForType() {
        return com.xyz.rpc.benchmark.PB.RequestObject.getDescriptor();
      }

      public com.xyz.rpc.benchmark.PB.RequestObject getDefaultInstanceForType() {
        return com.xyz.rpc.benchmark.PB.RequestObject.getDefaultInstance();
      }

      public com.xyz.rpc.benchmark.PB.RequestObject build() {
        com.xyz.rpc.benchmark.PB.RequestObject result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      private com.xyz.rpc.benchmark.PB.RequestObject buildParsed() throws com.google.protobuf.InvalidProtocolBufferException {
        com.xyz.rpc.benchmark.PB.RequestObject result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result).asInvalidProtocolBufferException();
        }
        return result;
      }

      public com.xyz.rpc.benchmark.PB.RequestObject buildPartial() {
        com.xyz.rpc.benchmark.PB.RequestObject result = new com.xyz.rpc.benchmark.PB.RequestObject(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.bytesObject_ = bytesObject_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.xyz.rpc.benchmark.PB.RequestObject) {
          return mergeFrom((com.xyz.rpc.benchmark.PB.RequestObject) other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(com.xyz.rpc.benchmark.PB.RequestObject other) {
        if (other == com.xyz.rpc.benchmark.PB.RequestObject.getDefaultInstance())
          return this;
        if (other.hasBytesObject()) {
          setBytesObject(other.getBytesObject());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }

      public final boolean isInitialized() {
        if (!hasBytesObject()) {

          return false;
        }
        return true;
      }

      public Builder mergeFrom(com.google.protobuf.CodedInputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        com.google.protobuf.UnknownFieldSet.Builder unknownFields = com.google.protobuf.UnknownFieldSet.newBuilder(this.getUnknownFields());
        while (true) {
          int tag = input.readTag();
          switch (tag) {
          case 0:
            this.setUnknownFields(unknownFields.build());
            onChanged();
            return this;
          default: {
            if (!parseUnknownField(input, unknownFields, extensionRegistry, tag)) {
              this.setUnknownFields(unknownFields.build());
              onChanged();
              return this;
            }
            break;
          }
          case 10: {
            bitField0_ |= 0x00000001;
            bytesObject_ = input.readBytes();
            break;
          }
          }
        }
      }

      private int bitField0_;

      // required bytes bytesObject = 1;
      private com.google.protobuf.ByteString bytesObject_ = com.google.protobuf.ByteString.EMPTY;

      public boolean hasBytesObject() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }

      public com.google.protobuf.ByteString getBytesObject() {
        return bytesObject_;
      }

      public Builder setBytesObject(com.google.protobuf.ByteString value) {
        if (value == null) {
          throw new NullPointerException();
        }
        bitField0_ |= 0x00000001;
        bytesObject_ = value;
        onChanged();
        return this;
      }

      public Builder clearBytesObject() {
        bitField0_ = (bitField0_ & ~0x00000001);
        bytesObject_ = getDefaultInstance().getBytesObject();
        onChanged();
        return this;
      }

      // @@protoc_insertion_point(builder_scope:com.bluedavy.RequestObject)
    }

    static {
      defaultInstance = new RequestObject(true);
      defaultInstance.initFields();
    }

    // @@protoc_insertion_point(class_scope:com.bluedavy.RequestObject)
  }

  public interface ResponseObjectOrBuilder extends com.google.protobuf.MessageOrBuilder {

    // required bytes bytesObject = 1;
    boolean hasBytesObject();

    com.google.protobuf.ByteString getBytesObject();
  }

  public static final class ResponseObject extends com.google.protobuf.GeneratedMessage implements ResponseObjectOrBuilder {
    // Use ResponseObject.newBuilder() to construct.
    private ResponseObject(Builder builder) {
      super(builder);
    }

    private ResponseObject(boolean noInit) {
    }

    private static final ResponseObject defaultInstance;

    public static ResponseObject getDefaultInstance() {
      return defaultInstance;
    }

    public ResponseObject getDefaultInstanceForType() {
      return defaultInstance;
    }

    public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
      return com.xyz.rpc.benchmark.PB.internal_static_com_bluedavy_ResponseObject_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
      return com.xyz.rpc.benchmark.PB.internal_static_com_bluedavy_ResponseObject_fieldAccessorTable;
    }

    private int bitField0_;
    // required bytes bytesObject = 1;
    public static final int BYTESOBJECT_FIELD_NUMBER = 1;
    private com.google.protobuf.ByteString bytesObject_;

    public boolean hasBytesObject() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }

    public com.google.protobuf.ByteString getBytesObject() {
      return bytesObject_;
    }

    private void initFields() {
      bytesObject_ = com.google.protobuf.ByteString.EMPTY;
    }

    private byte memoizedIsInitialized = -1;

    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1)
        return isInitialized == 1;

      if (!hasBytesObject()) {
        memoizedIsInitialized = 0;
        return false;
      }
      memoizedIsInitialized = 1;
      return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output) throws java.io.IOException {
      getSerializedSize();
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeBytes(1, bytesObject_);
      }
      getUnknownFields().writeTo(output);
    }

    private int memoizedSerializedSize = -1;

    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1)
        return size;

      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream.computeBytesSize(1, bytesObject_);
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }

    /*
     * (non-Javadoc)
     * @see com.google.protobuf.GeneratedMessage#getUnknownFields()
     */
    @Override
    public UnknownFieldSet getUnknownFields() {
      return UnknownFieldSet.getDefaultInstance();
    }

    private static final long serialVersionUID = 0L;

    @java.lang.Override
    protected java.lang.Object writeReplace() throws java.io.ObjectStreamException {
      return super.writeReplace();
    }

    public static com.xyz.rpc.benchmark.PB.ResponseObject parseFrom(com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }

    public static com.xyz.rpc.benchmark.PB.ResponseObject parseFrom(com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry) throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry).buildParsed();
    }

    public static com.xyz.rpc.benchmark.PB.ResponseObject parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }

    public static com.xyz.rpc.benchmark.PB.ResponseObject parseFrom(byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry) throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry).buildParsed();
    }

    public static com.xyz.rpc.benchmark.PB.ResponseObject parseFrom(java.io.InputStream input) throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }

    public static com.xyz.rpc.benchmark.PB.ResponseObject parseFrom(java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry) throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry).buildParsed();
    }

    public static com.xyz.rpc.benchmark.PB.ResponseObject parseDelimitedFrom(java.io.InputStream input) throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }

    public static com.xyz.rpc.benchmark.PB.ResponseObject parseDelimitedFrom(java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry) throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input, extensionRegistry)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }

    public static com.xyz.rpc.benchmark.PB.ResponseObject parseFrom(com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }

    public static com.xyz.rpc.benchmark.PB.ResponseObject parseFrom(com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry) throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry).buildParsed();
    }

    public static Builder newBuilder() {
      return Builder.create();
    }

    public Builder newBuilderForType() {
      return newBuilder();
    }

    public static Builder newBuilder(com.xyz.rpc.benchmark.PB.ResponseObject prototype) {
      return newBuilder().mergeFrom(prototype);
    }

    public Builder toBuilder() {
      return newBuilder(this);
    }

    @java.lang.Override
    protected Builder newBuilderForType(com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }

    public static final class Builder extends com.google.protobuf.GeneratedMessage.Builder<Builder>
        implements com.xyz.rpc.benchmark.PB.ResponseObjectOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
        return com.xyz.rpc.benchmark.PB.internal_static_com_bluedavy_ResponseObject_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
        return com.xyz.rpc.benchmark.PB.internal_static_com_bluedavy_ResponseObject_fieldAccessorTable;
      }

      // Construct using
      // code.google.nfs.rpc.benchmark.PB.ResponseObject.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }

      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
        }
      }

      private static Builder create() {
        return new Builder();
      }

      public Builder clear() {
        super.clear();
        bytesObject_ = com.google.protobuf.ByteString.EMPTY;
        bitField0_ = (bitField0_ & ~0x00000001);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor getDescriptorForType() {
        return com.xyz.rpc.benchmark.PB.ResponseObject.getDescriptor();
      }

      public com.xyz.rpc.benchmark.PB.ResponseObject getDefaultInstanceForType() {
        return com.xyz.rpc.benchmark.PB.ResponseObject.getDefaultInstance();
      }

      public com.xyz.rpc.benchmark.PB.ResponseObject build() {
        com.xyz.rpc.benchmark.PB.ResponseObject result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      private com.xyz.rpc.benchmark.PB.ResponseObject buildParsed() throws com.google.protobuf.InvalidProtocolBufferException {
        com.xyz.rpc.benchmark.PB.ResponseObject result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result).asInvalidProtocolBufferException();
        }
        return result;
      }

      public com.xyz.rpc.benchmark.PB.ResponseObject buildPartial() {
        com.xyz.rpc.benchmark.PB.ResponseObject result = new com.xyz.rpc.benchmark.PB.ResponseObject(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.bytesObject_ = bytesObject_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.xyz.rpc.benchmark.PB.ResponseObject) {
          return mergeFrom((com.xyz.rpc.benchmark.PB.ResponseObject) other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(com.xyz.rpc.benchmark.PB.ResponseObject other) {
        if (other == com.xyz.rpc.benchmark.PB.ResponseObject.getDefaultInstance())
          return this;
        if (other.hasBytesObject()) {
          setBytesObject(other.getBytesObject());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }

      public final boolean isInitialized() {
        if (!hasBytesObject()) {

          return false;
        }
        return true;
      }

      public Builder mergeFrom(com.google.protobuf.CodedInputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        com.google.protobuf.UnknownFieldSet.Builder unknownFields = com.google.protobuf.UnknownFieldSet.newBuilder(this.getUnknownFields());
        while (true) {
          int tag = input.readTag();
          switch (tag) {
          case 0:
            this.setUnknownFields(unknownFields.build());
            onChanged();
            return this;
          default: {
            if (!parseUnknownField(input, unknownFields, extensionRegistry, tag)) {
              this.setUnknownFields(unknownFields.build());
              onChanged();
              return this;
            }
            break;
          }
          case 10: {
            bitField0_ |= 0x00000001;
            bytesObject_ = input.readBytes();
            break;
          }
          }
        }
      }

      private int bitField0_;

      // required bytes bytesObject = 1;
      private com.google.protobuf.ByteString bytesObject_ = com.google.protobuf.ByteString.EMPTY;

      public boolean hasBytesObject() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }

      public com.google.protobuf.ByteString getBytesObject() {
        return bytesObject_;
      }

      public Builder setBytesObject(com.google.protobuf.ByteString value) {
        if (value == null) {
          throw new NullPointerException();
        }
        bitField0_ |= 0x00000001;
        bytesObject_ = value;
        onChanged();
        return this;
      }

      public Builder clearBytesObject() {
        bitField0_ = (bitField0_ & ~0x00000001);
        bytesObject_ = getDefaultInstance().getBytesObject();
        onChanged();
        return this;
      }

      // @@protoc_insertion_point(builder_scope:com.bluedavy.ResponseObject)
    }

    static {
      defaultInstance = new ResponseObject(true);
      defaultInstance.initFields();
    }

    // @@protoc_insertion_point(class_scope:com.bluedavy.ResponseObject)
  }

  private static com.google.protobuf.Descriptors.Descriptor internal_static_com_bluedavy_RequestObject_descriptor;
  private static com.google.protobuf.GeneratedMessage.FieldAccessorTable internal_static_com_bluedavy_RequestObject_fieldAccessorTable;
  private static com.google.protobuf.Descriptors.Descriptor internal_static_com_bluedavy_ResponseObject_descriptor;
  private static com.google.protobuf.GeneratedMessage.FieldAccessorTable internal_static_com_bluedavy_ResponseObject_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor getDescriptor() {
    return descriptor;
  }

  private static com.google.protobuf.Descriptors.FileDescriptor descriptor;
  static {
    java.lang.String[] descriptorData = {
        "\n\nTest.proto\022\014com.bluedavy\"$\n\rRequestObj" + "ect\022\023\n\013bytesObject\030\001 \002(\014\"%\n\016ResponseObje"
            + "ct\022\023\n\013bytesObject\030\001 \002(\014B#\n\035code.google.n" + "fs.rpc.benchmarkB\002PB" };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner = new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
      public com.google.protobuf.ExtensionRegistry assignDescriptors(com.google.protobuf.Descriptors.FileDescriptor root) {
        descriptor = root;
        internal_static_com_bluedavy_RequestObject_descriptor = getDescriptor().getMessageTypes().get(0);
        internal_static_com_bluedavy_RequestObject_fieldAccessorTable = new com.google.protobuf.GeneratedMessage.FieldAccessorTable(
            internal_static_com_bluedavy_RequestObject_descriptor, new java.lang.String[] { "BytesObject", },
            com.xyz.rpc.benchmark.PB.RequestObject.class, com.xyz.rpc.benchmark.PB.RequestObject.Builder.class);
        internal_static_com_bluedavy_ResponseObject_descriptor = getDescriptor().getMessageTypes().get(1);
        internal_static_com_bluedavy_ResponseObject_fieldAccessorTable = new com.google.protobuf.GeneratedMessage.FieldAccessorTable(
            internal_static_com_bluedavy_ResponseObject_descriptor, new java.lang.String[] { "BytesObject", },
            com.xyz.rpc.benchmark.PB.ResponseObject.class, com.xyz.rpc.benchmark.PB.ResponseObject.Builder.class);
        return null;
      }
    };
    com.google.protobuf.Descriptors.FileDescriptor.internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {}, assigner);
  }

  // @@protoc_insertion_point(outer_class_scope)
}
