import 'core-js/features/promise';

export default function (component, execute) {
  /* eslint-disable indent */
  switch (component) {
    default: break;
    case 'SampleComponent': import(`../../templates/components/SampleComponent/v1/SampleComponent/SampleComponent`).then(execute); break;
    case 'SampleComponent1': import(`../../templates/components/SampleComponent1/v1/SampleComponent1/SampleComponent1`).then(execute); break;
    case 'Test': import(`../../templates/components/Test/v1/Test/Test`).then(execute); break;
    /*{import}*/
  }
  /* eslint-enable indent */
}
