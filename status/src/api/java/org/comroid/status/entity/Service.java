package org.comroid.status.entity;

import org.comroid.common.Polyfill;
import org.comroid.common.func.Invocable;
import org.comroid.common.ref.IntEnum;
import org.comroid.common.ref.Specifiable;
import org.comroid.common.ref.WrappedFormattable;
import org.comroid.status.DependenyObject;
import org.comroid.status.StatusUpdater;
import org.comroid.uniform.ValueType;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.varbind.annotation.Location;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.comroid.varbind.container.DataContainerBase;

import java.util.Arrays;

@Location(Service.Bind.class)
public interface Service extends Entity, WrappedFormattable {
    default String getDisplayName() {
        return requireNonNull(Bind.DisplayName);
    }

    default Status getStatus() {
        return requireNonNull(Bind.Status);
    }

    @Override
    default String getDefaultFormattedName() {
        return getDisplayName();
    }

    @Override
    default String getAlternateFormattedName() {
        return getName();
    }

    enum Status implements IntEnum {
        UNKNOWN(0),

        OFFLINE(1),
        MAINTENANCE(2),
        REPORTED_PROBLEMS(3),
        ONLINE(4);

        private final int value;

        @Override
        public int getValue() {
            return value;
        }

        Status(int value) {
            this.value = value;
        }

        public static Status valueOf(int value) {
            return Arrays.stream(values())
                    .filter(it -> it.value == value)
                    .findAny()
                    .orElse(UNKNOWN);
        }

        @Override
        public String toString() {
            return name();
        }
    }

    interface Bind extends Entity.Bind {
        @RootBind
        GroupBind<Service, DependenyObject> Root
                = Entity.Bind.Root.subGroup("service", Invocable.ofConstructor(Polyfill.<Class<Service>>uncheckedCast(Basic.class)));
        VarBind.OneStage<String> DisplayName
                = Root.bind1stage("display_name", ValueType.STRING);
        VarBind.TwoStage<Integer, Status> Status
                = Root.bind2stage("status", ValueType.INTEGER, Service.Status::valueOf);
    }

    final class Basic extends DataContainerBase<DependenyObject> implements Service {
        public Basic(StatusUpdater updater, UniObjectNode node) {
            super(node, updater);
        }
    }
}
