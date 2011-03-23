package ru.kc.module.snapshots.event;

import ru.kc.module.snapshots.model.TreeNode;
import ru.kc.platform.domain.annotation.DomainSpecific;
import ru.kc.platform.event.Request;

@DomainSpecific
public class CreateTreeNodesRequest extends Request<TreeNode>{

}
