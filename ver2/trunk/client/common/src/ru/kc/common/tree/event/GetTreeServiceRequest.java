package ru.kc.common.tree.event;

import ru.kc.common.tree.TreeService;
import ru.kc.platform.domain.annotation.DomainSpecific;
import ru.kc.platform.event.Request;

@DomainSpecific
public class GetTreeServiceRequest extends Request<TreeService>{

}
