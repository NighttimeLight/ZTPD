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

//        1.30
//        EPDeployment deployment = compileAndDeploy(epRuntime,
//                "select istream data, spolka, kursOtwarcia " +
//                        "from KursAkcji(spolka='Oracle')#length(2) " +
//                        "having kursOtwarcia = max(kursOtwarcia) and count(*)>1");

//        2.4a
//        EPDeployment deployment = compileAndDeploy(epRuntime,
//                "select irstream data, kursZamkniecia, max(kursZamkniecia) " +
//                        "from KursAkcji(spolka = 'Oracle')#ext_timed(data.getTime(), 7 days)");
//        dopiero po pojawieniu się zdarzenia 'Sep 17' system dowiaduje się że zdarzenia 'Sep 5-10' wypadły z okna

//        2.4b
//        EPDeployment deployment = compileAndDeploy(epRuntime,
//                "select irstream data, kursZamkniecia, max(kursZamkniecia) " +
//                        "from KursAkcji(spolka = 'Oracle')#ext_timed_batch(data.getTime(), 7 days)");
//        zdarzenia 'Sep 19,20' pozostały w niewypełnionym oknie batch więc nie zostały przetworzone
//        ostatnie zdarzenia pozostały w oknie przy ostatnim zdarzeniu więc nie pojawiły się w RSTREAM

//        2.5
//        EPDeployment deployment = compileAndDeploy(epRuntime,
//                "select istream data, spolka, kursZamkniecia, max(kursZamkniecia)-kursZamkniecia as roznica " +
//                        "from KursAkcji#ext_timed_batch(data.getTime(), 1 days) ");

//        2.6
//        EPDeployment deployment = compileAndDeploy(epRuntime,
//                "select istream data, spolka, kursZamkniecia, max(kursZamkniecia)-kursZamkniecia as roznica " +
//                        "from KursAkcji(spolka in ('IBM', 'Honda', 'Microsoft'))#ext_timed_batch(data.getTime(), 1 days) ");

//        2.7a
//        EPDeployment deployment = compileAndDeploy(epRuntime,
//                "select istream data, spolka, kursOtwarcia, kursZamkniecia " +
//                        "from KursAkcji(kursZamkniecia>kursOtwarcia)#length(1)");

//        2.7b
//        EPDeployment deployment = compileAndDeploy(epRuntime,
//                "select istream data, spolka, kursOtwarcia, kursZamkniecia " +
//                        "from KursAkcji(KursAkcji.roznica(kursOtwarcia, kursZamkniecia)>0)#length(1)");

//        2.8
//        EPDeployment deployment = compileAndDeploy(epRuntime,
//                "select istream data, spolka, kursZamkniecia, max(kursZamkniecia)-kursZamkniecia as roznica " +
//                        "from KursAkcji(spolka in ('PepsiCo', 'CocaCola'))#ext_timed(data.getTime(), 7 days)");

//        2.9
//        EPDeployment deployment = compileAndDeploy(epRuntime,
//                "select istream data, spolka, kursZamkniecia " +
//                        "from KursAkcji(spolka in ('PepsiCo', 'CocaCola'))#ext_timed_batch(data.getTime(), 1 days) " +
//                        "having kursZamkniecia=max(kursZamkniecia)");

//        2.10
//        EPDeployment deployment = compileAndDeploy(epRuntime,
//                "select istream max(kursZamkniecia) as maksimum " +
//                        "from KursAkcji#ext_timed_batch(data.getTime(), 7 days) ");

//        2.11
//        EPDeployment deployment = compileAndDeploy(epRuntime,
//                "select istream pep.data, pep.kursZamkniecia as kursPep, coc.kursZamkniecia as kursCoc " +
//                        "from KursAkcji(spolka='PepsiCo')#length(1) pep, " +
//                            "KursAkcji(spolka='CocaCola')#length(1) coc " +
//                        "where pep.kursZamkniecia > coc.kursZamkniecia and " +
//                            "pep.data = coc.data");

//        2.12
//        EPDeployment deployment = compileAndDeploy(epRuntime,
//                "select istream k.data, k.spolka, k.kursZamkniecia as kursBiezacy, ref.kursZamkniecia-k.kursZamkniecia as roznica " +
//                        "from KursAkcji(spolka in ('PepsiCo', 'CocaCola'))#length(1) k, " +
//                            "KursAkcji(spolka in ('PepsiCo', 'CocaCola'))#firstunique(spolka) ref " +
//                        "where k.spolka=ref.spolka");

//        2.13
//        EPDeployment deployment = compileAndDeploy(epRuntime,
//                "select istream k.data, k.spolka, k.kursZamkniecia as kursBiezacy, ref.kursZamkniecia-k.kursZamkniecia as roznica " +
//                        "from KursAkcji#length(1) k, " +
//                            "KursAkcji#firstunique(spolka) ref " +
//                        "where k.spolka=ref.spolka and " +
//                        "k.kursZamkniecia > ref.kursZamkniecia");

//        2.14
//        EPDeployment deployment = compileAndDeploy(epRuntime,
//                "select istream a.data, b.data, a.spolka, " +
//                            "a.kursZamkniecia as kursA, b.kursZamkniecia as kursB " +
//                        "from KursAkcji#ext_timed(data.getTime(), 7 days) a, " +
//                            "KursAkcji#ext_timed(data.getTime(), 7 days) b " +
//                        "where a.spolka=b.spolka and " +
//                        "a.kursZamkniecia > (b.kursZamkniecia + 3.0)");

//        2.15
//        EPDeployment deployment = compileAndDeploy(epRuntime,
//                "select istream data, spolka, obrot " +
//                        "from KursAkcji(market='NYSE')#ext_timed_batch(data.getTime(), 7 days) " +
//                        "group by spolka " +
//                        "order by obrot desc " +
//                        "limit 3");

//        2.16
        EPDeployment deployment = compileAndDeploy(epRuntime,
                "select istream data, spolka, obrot " +
                        "from KursAkcji(market='NYSE')#ext_timed_batch(data.getTime(), 7 days) " +
                        "group by spolka " +
                        "order by obrot desc " +
                        "limit 2, 1");


        ProstyListener prostyListener = new ProstyListener();
        for (EPStatement statement : deployment.getStatements()) {
            statement.addListener(prostyListener);
        }

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
