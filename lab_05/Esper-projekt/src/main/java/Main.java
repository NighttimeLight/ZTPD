import com.espertech.esper.common.client.EPCompiled;
import com.espertech.esper.common.client.configuration.Configuration;
import com.espertech.esper.compiler.client.CompilerArguments;
import com.espertech.esper.compiler.client.EPCompileException;
import com.espertech.esper.compiler.client.EPCompilerProvider;
import com.espertech.esper.runtime.client.*;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Configuration configuration = new Configuration();
        configuration.getCommon().addEventType(KursAkcji.class);
        EPRuntime epRuntime = EPRuntimeProvider.getDefaultRuntime(configuration);

        EPDeployment deployment = compileAndDeploy(epRuntime,
                "create variable int currYear = 0;\n" +
                    "create variable int displayedYear = -1;\n" +
                    "create variable int windowSize = 50;\n" +
                    "create variable Long minLiczba = 0;\n" +
                    "create schema kursyLicznik(kursZamkniecia Integer, liczba Long, blad Integer);\n" +
                    "create window summary#sort(windowSize, liczba desc) as kursyLicznik;\n" +
                    "on KursAkcji(currYear != KursAkcji.getYear(data)) " +
                        "set currYear = KursAkcji.getYear(data);\n" +
                    "on KursAkcji as ka " +
                        "merge summary as sm " +
                        "where cast(sm.kursZamkniecia, int) = cast(ka.kursZamkniecia, int) " +
                        "when matched then " +
                            "update set sm.liczba = sm.liczba + 1 " +
                        "when not matched then " +
                            "insert into summary(kursZamkniecia, liczba, blad) " +
                            "select cast(ka.kursZamkniecia, int), minLiczba + 1, cast(minLiczba, int);\n" +
                    "on summary set minLiczba = min(liczba);\n" +
                    "@name('res') " +
                    "select sm.*, currYear as _year " +
                        "from summary sm " +
                        "output snapshot when displayedYear != currYear " +
                        "then set displayedYear = currYear " +
                        "limit 10;\n"
        );

        EPDeploymentService deploymentService = epRuntime.getDeploymentService();
        EPStatement statement = deploymentService
                .getStatement(deployment.getDeploymentId(), "res");
        ProstyListener prostyListener = new ProstyListener();
        statement.addListener(prostyListener);

        InputStream inputStream = new InputStream();
        inputStream.generuj(epRuntime.getEventService());
    }

    public static EPDeployment compileAndDeploy(EPRuntime epRuntime, String epl) {
        EPDeploymentService deploymentService = epRuntime.getDeploymentService();
        CompilerArguments args = new CompilerArguments(epRuntime.getConfigurationDeepCopy());
        EPDeployment deployment;
        try {
            EPCompiled epCompiled = EPCompilerProvider.getCompiler().compile(epl, args);
            deployment = deploymentService.deploy(epCompiled);
        } catch (EPCompileException e) {
            throw new RuntimeException(e);
        } catch (EPDeployException e) {
            throw new RuntimeException(e);
        }
        return deployment;
    }
}
