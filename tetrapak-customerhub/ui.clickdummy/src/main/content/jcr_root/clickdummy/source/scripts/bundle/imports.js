import 'core-js/features/promise';

export default function (component, execute) {
  /* eslint-disable indent */
  switch (component) {
    case 'SampleComponent': import(`../../templates/components/SampleComponent/v1/SampleComponent/SampleComponent`).then(execute); break;
    case 'SampleComponent1': import(`../../templates/components/SampleComponent1/v1/SampleComponent1/SampleComponent1`).then(execute); break;
    case 'TestComponent': import(`../../templates/components/TestComponent/v1/TestComponent/TestComponent`).then(execute); break;
    default: break;
  }
  /* eslint-enable indent */
}
