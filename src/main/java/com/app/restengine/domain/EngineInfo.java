package com.app.restengine.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EngineInfo {
    public int corePoolSize;
    public int maxPoolSize;
    public int activeCount;
    public long taskCount;
    public long taskQueueSize;
    public List<TaskInfo> tasks = new ArrayList<>();
}
