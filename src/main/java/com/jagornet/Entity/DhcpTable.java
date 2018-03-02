package com.jagornet.Entity;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @Author:LiuXinJian
 * @Description:
 * @Date:Created in 10:32 2018/2/26
 */

public class DhcpTable {

    private String ipaddress;
    private byte[] duid;
    private Double iatype;
    private String iaid;
    private String prefixlen;
    private Double state;
    private String starttime;
    private String preferredEndTime;
    private Double validendtime;
    private Collection<DhcpOption> iaDhcpOptions;
    private Collection<DhcpOption> iaAddrDhcpOptions;

    public String getIpaddress() {
        return ipaddress;
    }

    public void setIpaddress(String ipaddress) {
        this.ipaddress = ipaddress;
    }

    public byte[] getDuid() {
        return duid;
    }

    public void setDuid(byte[] duid) {
        this.duid = duid;
    }

    public Double getIatype() {
        return iatype;
    }

    public void setIatype(Double iatype) {
        this.iatype = iatype;
    }

    public String getIaid() {
        return iaid;
    }

    public void setIaid(String iaid) {
        this.iaid = iaid;
    }

    public String getPrefixlen() {
        return prefixlen;
    }

    public void setPrefixlen(String prefixlen) {
        this.prefixlen = prefixlen;
    }

    public Double getState() {
        return state;
    }

    public void setState(Double state) {
        this.state = state;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getPreferredEndTime() {
        return preferredEndTime;
    }

    public void setPreferredEndTime(String preferredEndTime) {
        this.preferredEndTime = preferredEndTime;
    }

    public Double getValidendtime() {
        return validendtime;
    }

    public void setValidendtime(Double validendtime) {
        this.validendtime = validendtime;
    }

    public Collection<DhcpOption> getIaDhcpOptions() {
        return iaDhcpOptions;
    }

    public void setIaDhcpOptions(Collection<DhcpOption> iaDhcpOptions) {
        this.iaDhcpOptions = iaDhcpOptions;
    }

    public void setIaDhcpOptions(byte[] buf) {
        if (buf != null && buf.length > 0)
        this.iaDhcpOptions = decodeOptions(buf);
    }
    public Collection<DhcpOption> getIaAddrDhcpOptions() {
        return iaAddrDhcpOptions;
    }

    public void setIaAddrDhcpOptions(Collection<DhcpOption> iaAddrDhcpOptions) {
        this.iaAddrDhcpOptions = iaAddrDhcpOptions;
    }

    public void setIaAddrDhcpOptions(byte[] buf) {
        if (buf != null && buf.length > 0)
            this.iaAddrDhcpOptions = decodeOptions(buf);
    }
    public Collection<DhcpOption> decodeOptions(byte[] buf) {
        Collection<DhcpOption> options = null;
        if (buf != null) {
            ByteBuffer bb = ByteBuffer.wrap(buf);
            options = new ArrayList<DhcpOption>();
            while (bb.hasRemaining()) {
                DhcpOption option = new DhcpOption();
                option.setCode(bb.getShort());
                int len = bb.getShort();
                byte[] val = new byte[len];
                bb.get(val);
                option.setValue(val);
                options.add(option);
            }
        }
        return options;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ipaddress:" + getIpaddress());
        sb.append("iaid:" + getIaid());
        sb.append("duid:" + getDuid());
        sb.append("iatype:" + getIatype());
        sb.append("prefixlen:" + getPrefixlen());
        sb.append("state:" + getState());
        sb.append("statetime:" + getStarttime());
        sb.append("preferredEndTime:" + getPreferredEndTime());
        sb.append("validendtime:" + getValidendtime());
        if (getIaAddrDhcpOptions() != null && !getIaAddrDhcpOptions().isEmpty()) {
            sb.append("iaAddrDhcpOptions:...");
        }
        if (getIaDhcpOptions() != null && !getIaDhcpOptions().isEmpty()) {
            sb.append("iaDhcpOptions:...");
        }
        return sb.toString();
    }
}
